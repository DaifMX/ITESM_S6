**AI in Java**

**Assignment 2 + Final Project**

CRUD Generator Agent with RAG

Multi-Agent System using Ollama + LangChain4j + Vector RAG

Assignment 2 — Due before Class 3 | Final — Due before Class 4

# **Project Overview**

You will build a CRUD Code Generator powered by a multi-agent AI system. The user describes an entity in plain English — for example, "A Product with name, price, category, and stock quantity" — and the system generates a complete, working Spring Boot + JPA CRUD implementation including unit tests.

The system has four specialized agents, each with a focused responsibility:

|     |     |
| --- | --- |
| **Agent** | **Responsibility** |
| **CodeGeneratorAgent** | Receives an entity description in plain English. Generates: Entity.java (JPA), Repository.java, Service.java, Controller.java (Spring Boot REST). Uses qwen2.5:3b or codellama. |
| **TestGeneratorAgent** | Receives any .java class. Generates JUnit 5 + Mockito unit tests covering happy path, edge cases, and exceptions. Uses qwen2.5-coder:7b. |
| **RAGContextAgent** | Before generating code, retrieves relevant project context from the vector store: existing entities, naming conventions, package structure. Feeds context into the code generation prompt. |
| **OrchestratorAgent** | Coordinates the full pipeline: parses user input, calls RAGContextAgent for context, calls CodeGeneratorAgent, then calls TestGeneratorAgent. Returns the complete output. |

# **Technology Stack**

All tools are free and run locally. No API keys or cloud accounts required.

## **Core dependencies (pom.xml)**

- Java 17+
- Spring Boot 3.x — REST API layer and dependency injection
- LangChain4j 0.32+ — agent framework, tool calling, embedding models
- LangChain4j Ollama integration — connects agents to local Ollama models
- LangChain4j in-memory embedding store — our vector store (no database required for Assignment 2)
- Nomic Embed Text (via Ollama) — local embedding model for RAG
- H2 Database — in-memory database for storing generated entity metadata
- Jackson — JSON serialization
- JUnit 5 + Mockito — test framework

## **Ollama models to pull**

ollama pull qwen2.5:3b # main code generation model

ollama pull qwen2.5-coder:7b # specialized for test generation (if RAM allows)

ollama pull nomic-embed-text # embedding model for RAG

⚠️ If your machine has less than 8 GB of RAM, use qwen2.5:3b for all agents. Quality is slightly lower but it works.

# **Assignment 2 — Build the Core Agents**

Assignment 2 covers Class 2 content: RAG context retrieval and the two primary generation agents. You will implement the RAGContextAgent and CodeGeneratorAgent.

## **Task 2.1 — Project Structure**

Create the following Maven project structure:

crud-generator/

src/main/java/com/ai/crud/

agents/

RAGContextAgent.java

CodeGeneratorAgent.java

TestGeneratorAgent.java

OrchestratorAgent.java

rag/

DocumentIngester.java

VectorStoreService.java

model/

EntitySpec.java

GeneratedCRUD.java

config/

OllamaConfig.java

CrudGeneratorApplication.java

src/main/resources/

knowledge-base/ ← put .txt files here for RAG

application.yml

## **Task 2.2 — OllamaConfig.java**

Configure the LangChain4j Ollama chat model and embedding model as Spring beans:

@Configuration

public class OllamaConfig {

@Bean

public ChatLanguageModel codingModel() {

return OllamaChatModel.builder()

.baseUrl("http://localhost:11434")

.modelName("qwen2.5:3b")

.temperature(0.2) // low temp = more deterministic code

.timeout(Duration.ofMinutes(3))

.build();

}

@Bean

public EmbeddingModel embeddingModel() {

return OllamaEmbeddingModel.builder()

.baseUrl("http://localhost:11434")

.modelName("nomic-embed-text")

.build();

}

@Bean

public EmbeddingStore&lt;TextSegment&gt; embeddingStore() {

return new InMemoryEmbeddingStore<>();

}

}

## **Task 2.3 — Build the Knowledge Base for RAG**

The RAG system uses a local knowledge base of text files that represent your project's conventions. Create these files in src/main/resources/knowledge-base/:

**java-conventions.txt**

Package structure: com.ai.crud.entity, com.ai.crud.repository

com.ai.crud.service, com.ai.crud.controller

All entities must use @Entity and @Table(name = ...) annotations.

Use Long as the primary key type with @GeneratedValue(strategy = AUTO).

Repositories extend JpaRepository&lt;Entity, Long&gt;.

Service classes are annotated with @Service and @Transactional.

Controllers use @RestController and @RequestMapping("/api/v1/{entity}").

**existing-entities.txt (update this as you generate more entities)**

Existing entity: Customer

Fields: id (Long), name (String), email (String), phone (String), createdAt (LocalDateTime)

Existing entity: Order

Fields: id (Long), customer (Customer FK), total (BigDecimal), status (Enum: PENDING/SHIPPED/DELIVERED)

💡 This knowledge base is what makes RAG powerful. Before generating code for 'Product', the agent retrieves conventions and existing entities so the generated code is consistent with your project.

## **Task 2.4 — DocumentIngester.java**

This class reads all .txt files from the knowledge base and loads them into the vector store at application startup:

@Component

public class DocumentIngester {

@Autowired EmbeddingModel embeddingModel;

@Autowired EmbeddingStore&lt;TextSegment&gt; store;

@PostConstruct

public void ingest() throws Exception {

// TODO: Read all .txt files from resources/knowledge-base/

// TODO: Split into segments using DocumentSplitters.recursive(500, 50)

// TODO: Generate embeddings for each segment

// TODO: Add to embeddingStore

// Hint: use EmbeddingStoreIngestor from LangChain4j

}

}

## **Task 2.5 — RAGContextAgent.java**

This agent takes a user description and retrieves the most relevant context from the vector store:

@Service

public class RAGContextAgent {

@Autowired EmbeddingModel embeddingModel;

@Autowired EmbeddingStore&lt;TextSegment&gt; store;

public String retrieveContext(String entityDescription) {

// TODO: Embed the entityDescription

// TODO: Search store for top 5 relevant segments

// TODO: Concatenate and return as a single context string

// Hint: EmbeddingStoreRetriever.from(store, embeddingModel, 5)

return "";

}

}

## **Task 2.6 — CodeGeneratorAgent.java**

This is the core agent. It uses the retrieved context plus the user description to generate Java code:

@Service

public class CodeGeneratorAgent {

@Autowired ChatLanguageModel model;

@Autowired RAGContextAgent ragAgent;

public GeneratedCRUD generate(String entityDescription) {

String context = ragAgent.retrieveContext(entityDescription);

String prompt = """

You are a senior Java developer. Use these project conventions:

%s

Generate a complete Spring Boot CRUD for: %s

Return ONLY valid Java code. Generate four files, each starting with:

\=== Entity.java ===

\=== Repository.java ===

\=== Service.java ===

\=== Controller.java ===

""".formatted(context, entityDescription);

String response = model.generate(prompt);

return parseResponse(response); // parse into GeneratedCRUD object

}

}

## **Assignment 2 — Evidence to Submit**

Submit your Maven project as a .zip with an EVIDENCE.md containing:

1.  Screenshot showing the RAG retrieval working (add a debug log that prints retrieved segments)
2.  The full generated Java code for at least 2 different entities
3.  A comparison: run the CodeGeneratorAgent WITHOUT RAG context (empty string) and WITH RAG context for the same entity. Show the difference in the generated code.
4.  Answer: Which RAG segments were most useful? Why?

# **Final Project — Complete Multi-Agent System**

The Final Project adds the TestGeneratorAgent and OrchestratorAgent, exposes everything as a REST API, and includes evaluation of the generated code quality.

## **Task F.1 — TestGeneratorAgent.java**

Given the generated Java classes, this agent creates JUnit 5 + Mockito test classes:

@Service

public class TestGeneratorAgent {

@Autowired ChatLanguageModel model;

public String generateTests(String javaSourceCode, String className) {

String prompt = """

You are an expert in Java testing. Generate comprehensive JUnit 5 tests

for the following class. Requirements:

\- Use @ExtendWith(MockitoExtension.class)

\- Mock all dependencies with @Mock

\- Cover: happy path, null inputs, edge cases, exception scenarios

\- Use descriptive test method names (given_when_then pattern)

\- Minimum 5 test methods for Service classes

Class to test:

%s

""".formatted(javaSourceCode);

return model.generate(prompt);

}

}

## **Task F.2 — OrchestratorAgent.java**

The orchestrator ties everything together:

@Service

public class OrchestratorAgent {

@Autowired CodeGeneratorAgent codeAgent;

@Autowired TestGeneratorAgent testAgent;

public CRUDResult generateComplete(String entityDescription) {

// Step 1: Generate CRUD code (RAG is called internally)

GeneratedCRUD crud = codeAgent.generate(entityDescription);

// Step 2: Generate tests for the Service class

String serviceTests = testAgent.generateTests(

crud.getServiceCode(), crud.getEntityName() + "Service");

// Step 3: Update knowledge base with new entity

updateKnowledgeBase(crud);

return new CRUDResult(crud, serviceTests);

}

}

## **Task F.3 — REST API with Spring Boot**

Expose the orchestrator as a REST API:

@RestController

@RequestMapping("/api/generator")

public class GeneratorController {

@Autowired OrchestratorAgent orchestrator;

@PostMapping("/generate")

public ResponseEntity&lt;CRUDResult&gt; generate(@RequestBody GenerateRequest req) {

CRUDResult result = orchestrator.generateComplete(req.getEntityDescription());

return ResponseEntity.ok(result);

}

}

// Request: { "entityDescription": "A Product with name, price, stock" }

## **Task F.4 — Save files to disk**

Add a FileWriterService that saves the generated files to a configurable output directory:

- src/generated/{EntityName}/Entity.java
- src/generated/{EntityName}/Repository.java
- src/generated/{EntityName}/Service.java
- src/generated/{EntityName}/Controller.java
- src/generated/{EntityName}/ServiceTest.java

💡 Bonus: After saving, also update the knowledge-base/existing-entities.txt with the new entity so future RAG calls know about it. This makes the system learn from its own output!

## **Task F.5 — RAG Enhancement (Vector Persistence)**

For the final project, upgrade from InMemoryEmbeddingStore to a file-backed store so the knowledge base survives application restarts:

- Option A: Serialize the InMemoryEmbeddingStore to a JSON file on shutdown, reload on startup
- Option B (advanced): Use ChromaDB with Docker — docker run -p 8000:8000 chromadb/chroma
- LangChain4j has a ChromaEmbeddingStore class — replace InMemoryEmbeddingStore with it

## **Final Project — Evidence to Submit**

1.  ZIP of the complete Maven project with all agents implemented
2.  Video or screenshots of the REST API: call /api/generator/generate with 3 different entities and show the output
3.  The generated files saved to disk for each entity
4.  At least one generated test class that actually compiles and runs (copy it into a real project and run mvn test)
5.  REFLECTION.md (minimum 300 words) answering: How did RAG improve code quality? What were the model's biggest mistakes? What would you improve if you had more time?

## **Final Grading Criteria**

|     |     |     |
| --- | --- | --- |
| **Criteria** | **Weight** | **Points** |
| Assignment 2: RAGContextAgent + CodeGeneratorAgent working | 25% | 25  |
| Final: TestGeneratorAgent generating valid tests | 15% | 15  |
| Final: OrchestratorAgent pipeline end-to-end | 15% | 15  |
| Final: REST API working (POST /generate returns code) | 15% | 15  |
| Final: Files saved to disk correctly | 10% | 10  |
| Final: RAG persistence (file or ChromaDB) | 5%  | 5   |
| REFLECTION.md quality and depth | 10% | 10  |
| Code quality, structure, and comments | 5%  | 5   |

# **Architecture Diagram (Text)**

Study this flow before you start coding:

User Input: "A Product with name, price, category, stock"

|

v

OrchestratorAgent

|

|---> RAGContextAgent

| |---> EmbeddingModel (nomic-embed-text via Ollama)

| |---> VectorStore (InMemory / ChromaDB)

| | \[retrieves: Java conventions, existing entities\]

| v

| context string

|

|---> CodeGeneratorAgent

| |---> ChatLanguageModel (qwen2.5:3b via Ollama)

| |---> prompt = context + entity description

| v

| GeneratedCRUD (Entity, Repo, Service, Controller)

|

|---> TestGeneratorAgent

| |---> ChatLanguageModel (qwen2.5:3b via Ollama)

| v

| Test code for Service class

|

|---> FileWriterService (saves .java files to disk)

|---> Updates knowledge-base/existing-entities.txt

v

CRUDResult returned via REST API

✅ If you implement all tasks above, you will have built a real AI-powered developer tool that generates production-ready Spring Boot code from a single English sentence.