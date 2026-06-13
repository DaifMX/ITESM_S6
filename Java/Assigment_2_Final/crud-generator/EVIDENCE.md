# EVIDENCE — CRUD Generator Agent with RAG

Multi-agent CRUD generator built on **Ollama + LangChain4j + vector RAG**, exposed as a Spring Boot REST API.

- **Chat model:** `qwen2.5:3b` (temperature 0.2)
- **Embedding model:** `nomic-embed-text`
- **Vector store:** LangChain4j `InMemoryEmbeddingStore`, serialized to `data/embedding-store.json` (Task F.5, Option A)
- **Run:** `mvn spring-boot:run` (needs a local Ollama daemon on `:11434` with the three models pulled)

All raw artifacts referenced below live in the [`evidence/`](evidence/) folder and in [`src/generated/`](src/generated/).

---

## Assignment 2 evidence

### 1. RAG retrieval working

A debug log in [`RAGContextAgent`](src/main/java/com/ai/crud/agents/RAGContextAgent.java) prints every retrieved segment with its cosine score. Full log: [`evidence/rag-retrieval-log.txt`](evidence/rag-retrieval-log.txt). The raw JSON of the retrieved context is in [`evidence/rag-retrieval.json`](evidence/rag-retrieval.json).

Query: `"A Product with name, price, category, and stock quantity"`

```
RAG retrieved 5 segment(s) for query: "A Product with name, price, category, and stock quantity"
  [1] score=0.769 :: EXISTING ENTITIES IN THE PROJECT Existing entity: Customer Table: customers Fields: - id (Long...
  [2] score=0.757 :: Note: new entities should follow the same field naming and primary-key conventions as the entities...
  [3] score=0.744 :: PROJECT JAVA CONVENTIONS Package structure: - Entities live in com.ai.crud.entity - Repositories...
  [4] score=0.741 :: - Controllers live in com.ai.crud.controller Entity rules: - All entities must use the @Entity...
  [5] score=0.737 :: Repository rules: - Repositories are interfaces that extend JpaRepository<Entity, Long>...
```

You can reproduce this live: `GET http://localhost:8080/api/generator/rag?q=<your description>`.

### 2. Full generated Java for ≥ 2 entities

Three entities were generated end-to-end and written to disk under [`src/generated/`](src/generated/):

| Entity | Files | Raw API response |
| --- | --- | --- |
| **Product** | [`src/generated/Product/`](src/generated/Product/) | [`evidence/generate-product.json`](evidence/generate-product.json) |
| **Book** | [`src/generated/Book/`](src/generated/Book/) | [`evidence/generate-book.json`](evidence/generate-book.json) |
| **Employee** | [`src/generated/Employee/`](src/generated/Employee/) | [`evidence/generate-employee.json`](evidence/generate-employee.json) |

Example — `Product/Entity.java` (note the conventions absorbed from RAG: `@Table(name="products")`, `Long id`, `BigDecimal price`):

```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private Integer stockQuantity;
    // Getters and Setters
}
```

Example — `Book/Service.java` (full CRUD, constructor injection, `@Service @Transactional`, `EntityNotFoundException` — all RAG conventions):

```java
@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    public BookService(BookRepository bookRepository) { this.bookRepository = bookRepository; }

    public Book createBook(Book book) { return bookRepository.save(book); }
    public Optional<Book> findById(Long id) throws EntityNotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + id));
    }
    public List<Book> findAll() { return (List<Book>) bookRepository.findAll(); }
    public Book updateBook(Book book, Long id) throws EntityNotFoundException { ... }
    public void deleteBook(Long id) throws EntityNotFoundException { ... }
}
```

### 3. Comparison — WITHOUT RAG vs WITH RAG (same entity)

The [`/api/generator/compare`](src/main/java/com/ai/crud/controller/GeneratorController.java) endpoint generates the *same* description twice — once with the empty context, once with retrieved context. Raw output: [`evidence/compare-vehicle.json`](evidence/compare-vehicle.json).

Description: `"A Vehicle with make, model, year, and price"`

**WITHOUT RAG** (empty context):
```java
@Entity
public class Vehicle {
    @Id
    private Long id;
    private String make;
    private String model;
    private int year;
    private double price;     // <-- double, no @Table, no @GeneratedValue
    // getters and setters
}
```

**WITH RAG** (retrieved conventions injected):
```java
@Entity
@Table(name = "vehicles")     // <-- @Table added (convention)
public class Vehicle {
    @Id
    private Long id;
    private String make;
    private String model;
    private int year;
    private BigDecimal price;  // <-- BigDecimal for money (convention)
    // getters and setters
}
```

**Observable differences caused by RAG:**

| Aspect | Without RAG | With RAG |
| --- | --- | --- |
| Table mapping | none | `@Table(name = "vehicles")` |
| Money type | `double price` | `BigDecimal price` |
| Naming/structure | ad-hoc | matches Customer/Order conventions |

### 4. Which RAG segments were most useful? Why?

Ranked by impact on the generated code:

1. **`java-conventions.txt` — Entity rules.** The single highest-leverage segment. It is the *only* reason `@Table(name=...)`, `BigDecimal` for money, `Long` ids, `@GeneratedValue`, and the `jakarta.persistence.*` imports appear. The without-RAG run proves the model does *not* do these by default.
2. **`java-conventions.txt` — Service rules.** Produced `@Service @Transactional`, constructor injection, and the `EntityNotFoundException`-on-missing-id behaviour visible in `BookService`.
3. **`existing-entities.txt` (Customer / Order).** Gave the model a concrete naming template (`id`, `createdAt`, FK style), so new fields are named consistently with the rest of the project.

The repository/controller convention segments mattered less — a 3b model already knows the `extends JpaRepository<E, Long>` and `@RestController` shapes, so RAG mostly reinforced rather than corrected them.

---

## Final Project evidence

### F.1–F.2 — TestGeneratorAgent + OrchestratorAgent (end-to-end pipeline)

The full pipeline (RAG → code → tests → knowledge-base update → disk) runs from a single POST. Each `generate-*.json` above contains both `crud` (4 files) and `serviceTests`. Server-side pipeline logging is in [`evidence/app-startup-and-run.log`](evidence/app-startup-and-run.log).

### F.3 — REST API working

`POST /api/generator/generate` was called for 3 entities, all `HTTP 200`:

```
Product   -> HTTP 200 in 25.4s   (first call: cold model load)
Book      -> HTTP 200 in  5.0s
Employee  -> HTTP 200 in  2.9s
```

### F.4 — Files saved to disk

15 files written (5 per entity) under [`src/generated/`](src/generated/):
```
src/generated/{Product,Book,Employee}/{Entity,Repository,Service,Controller,ServiceTest}.java
```

### Bonus — knowledge base learns from its own output

After each generation the orchestrator appends the new entity to `existing-entities.txt` **and** ingests it into the live vector store. By the time the *Vehicle* query ran, RAG was already retrieving the previously-generated **Product** and **Book** entities (see `evidence/rag-retrieval-log.txt`):
```
RAG retrieved 5 segment(s) for query: "A Vehicle with make, model, year, and price"
  [1] score=0.766 :: Existing entity: Product Generated from description ...
  [2] score=0.748 :: Existing entity: Book Generated from description ...
```

### F.5 — RAG persistence (file-backed store)

Option A implemented: the `InMemoryEmbeddingStore` is serialized to `data/embedding-store.json` after every update and on shutdown (`@PreDestroy`), and reloaded on startup.

- **Persist** (`evidence/app-startup-and-run.log`): `Persisted vector store to .../data/embedding-store.json` (88 KB).
- **Reload on a fresh boot** (`evidence/app-restart-reload.log`):
  ```
  Reloading persisted embedding store from .../data/embedding-store.json
  Persisted vector store present at data/embedding-store.json — skipping knowledge-base ingestion
  ```
- **Data survived:** after the restart, `GET /api/generator/rag?q=Product entity` still returned the previously-learned `Existing entity: Product` segment — confirming the vectors, not just the file, persisted.

### A generated test that compiles and runs (`mvn test` green)

The AI-generated `EmployeeServiceTest` was copied into a standalone Maven project at [`evidence/generated-test-verification/`](evidence/generated-test-verification/) and executed:

```
cd evidence/generated-test-verification && mvn test
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

The generated service and repository were used **verbatim**. The only edits to the generated test (each flagged with a `// FIX:` comment in the source) were:
1. Two missing static imports (`assertThrows`, `Optional`) the model forgot.
2. Filling in the getters/setters the model left as a `// Getters and Setters` comment in the entity.
3. Correcting two assertions where the generated test *asserted* a `NullPointerException` that the generated service does not actually throw (`save(null)` on a mock is silent; `findById(null)` yields `Optional.empty()`). These are real generated-test logic flaws — documented, not hidden.

The project's own offline parser tests also pass: `mvn test` in the root project → `Tests run: 2, Failures: 0`.
