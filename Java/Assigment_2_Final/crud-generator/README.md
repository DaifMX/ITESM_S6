# crud-generator

A **CRUD Code Generator** powered by a local multi-agent AI system. Describe an entity in plain
English and it generates a complete Spring Boot + JPA CRUD (Entity, Repository, Service, Controller)
plus JUnit 5 + Mockito tests — using RAG over a project knowledge base so the output follows your
conventions.

Everything runs locally on **Ollama + LangChain4j**. No API keys, no cloud.

## Agents

| Agent | Responsibility |
| --- | --- |
| [`RAGContextAgent`](src/main/java/com/ai/crud/agents/RAGContextAgent.java) | Embeds the request, retrieves the top-5 relevant knowledge-base segments from the vector store. |
| [`CodeGeneratorAgent`](src/main/java/com/ai/crud/agents/CodeGeneratorAgent.java) | Combines RAG context + description → 4 Java files (`qwen2.5:3b`). |
| [`TestGeneratorAgent`](src/main/java/com/ai/crud/agents/TestGeneratorAgent.java) | Generates a JUnit 5 + Mockito test class for the Service. |
| [`OrchestratorAgent`](src/main/java/com/ai/crud/agents/OrchestratorAgent.java) | Runs the full pipeline, updates the knowledge base, writes files to disk. |

## Prerequisites

- Java 17+ and Maven
- [Ollama](https://ollama.com) running locally, with the models pulled:
  ```bash
  ollama pull qwen2.5:3b
  ollama pull qwen2.5-coder:7b   # optional, for higher-quality tests
  ollama pull nomic-embed-text
  ```

## Run

```bash
mvn spring-boot:run        # starts on http://localhost:8080
```

## REST API

```bash
# Full pipeline: code + tests + save to disk + learn
curl -X POST http://localhost:8080/api/generator/generate \
  -H 'Content-Type: application/json' \
  -d '{"entityDescription":"A Product with name, price, category, and stock quantity"}'

# Debug: see what RAG retrieves
curl 'http://localhost:8080/api/generator/rag?q=A%20Product%20with%20price'

# Debug: compare generation with vs without RAG
curl -X POST http://localhost:8080/api/generator/compare \
  -H 'Content-Type: application/json' \
  -d '{"entityDescription":"A Vehicle with make, model, year, and price"}'
```

Generated files are written to [`src/generated/{EntityName}/`](src/generated/).

## Configuration

See [`application.yml`](src/main/resources/application.yml) — Ollama URL/models, vector-store path,
output directory, and knowledge-base file are all configurable under the `app.*` keys.

## Tests

```bash
mvn test                                          # offline parser unit tests
cd evidence/generated-test-verification && mvn test   # an AI-generated test, running green
```

## Documents

- [`EVIDENCE.md`](EVIDENCE.md) — all Assignment 2 + Final Project evidence.
- [`REFLECTION.md`](REFLECTION.md) — RAG impact, model mistakes, future work.
