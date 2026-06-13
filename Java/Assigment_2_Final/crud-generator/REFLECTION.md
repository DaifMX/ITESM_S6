# REFLECTION

## How did RAG improve code quality?

RAG turned a generic code generator into a *project-aware* one. The clearest proof is the
with/without comparison in `EVIDENCE.md`: given the identical prompt "A Vehicle with make, model,
year, and price," the model with an empty context produced a bare `@Entity` class using a
`double` for price and no table mapping. The same model, handed five retrieved knowledge-base
segments, produced `@Table(name = "vehicles")` and switched `price` to `BigDecimal` — purely
because the retrieved "Entity rules" segment told it to use `@Table` and `BigDecimal` for money.
Nothing in the user's sentence requested either change; RAG supplied the missing project
convention.

The effect compounded across the layers. The Service classes came back annotated with
`@Service` and `@Transactional`, using constructor injection and throwing `EntityNotFoundException`
on a missing id — all of which are written verbatim in `java-conventions.txt`. The retrieved
`existing-entities.txt` (Customer, Order) also acted as a few-shot example: new entities inherited
the same `id`/`createdAt` naming and primary-key style. The most valuable single segment was the
Entity-rules block, because it corrected behaviour the base model otherwise gets wrong; the
repository/controller conventions mattered less, since a code model already defaults to
`extends JpaRepository` and `@RestController`. A nice emergent property: because the orchestrator
feeds each newly generated entity back into the vector store, later requests retrieved earlier
generations — by the fourth entity, RAG was surfacing the Product and Book it had just created.
The system genuinely learns from its own output.

## What were the model's biggest mistakes?

`qwen2.5:3b` is small, and it showed. The recurring failures:

1. **Omitted imports and boilerplate.** Almost every Entity left `// Getters and Setters` as a
   literal comment instead of generating the methods, and several files dropped the `package` line
   and imports — so the raw output rarely compiles without touch-up.
2. **Incorrect generated tests.** The `EmployeeServiceTest` asserted a `NullPointerException` on
   `save(null)` and `findById(null)` that the generated service never throws. The test *looked*
   plausible but encoded behaviour the code did not have — a reminder that LLM-written tests must
   themselves be verified, not trusted.
3. **Subtle type errors.** `BookService.findById` was declared to return `Optional<Book>` but its
   body used `orElseThrow(...)`, which returns a `Book` — a type mismatch that would fail to
   compile. The model mixed two valid patterns into one invalid one.
4. **Inconsistent depth.** Book got a full five-method CRUD service; Employee got only
   save/find. Output quality varied request to request even at temperature 0.2.

## What would I improve with more time?

- **A validation/repair loop:** compile each generated file (in-memory `JavaCompiler`) and feed
  errors back to the model for a second pass, so output is guaranteed to build.
- **Structured output instead of `=== marker ===` parsing:** ask for JSON (or use LangChain4j's
  `AiServices` typed interfaces) to remove the brittle regex parser.
- **Use the bigger `qwen2.5-coder:7b` for generation,** not just tests — it was pulled but I kept
  `3b` as the default for speed; the type and import errors above would likely drop sharply.
- **Richer retrieval:** chunk the knowledge base by topic and retrieve per-layer (entity rules for
  the entity prompt, controller rules for the controller prompt) instead of one shared context blob.
- **Swap the file-backed store for ChromaDB** (Option B) so retrieval scales past an in-memory map
  and supports metadata filtering.
