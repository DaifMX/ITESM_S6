# SpringBoot Gateway — Ollama AI Proxy

Spring Boot WebFlux application that acts as a reactive gateway between a frontend client and a local [Ollama](https://ollama.com) inference server. Exposes a simple chat REST API while abstracting all AI engine details behind a clean service layer.

## Architecture

```
Client
  │
  ▼
ChatController  (/api/v1/chat)
  │
  ▼
ChatService     (model selection, response mapping)
  │
  ▼
OllamaClient    (WebClient → Ollama /api/generate)
  │
  ▼
Ollama Server   (localhost:11434)
```

## Project Structure

```
src/main/java/net/daifo/springbootgateway/
├── SpringbootGatewayApplication.java   # Entry point
├── config/
│   └── WebClientConfig.java            # Configures reactive WebClient
├── controller/
│   └── ChatController.java             # POST /api/v1/chat  |  GET /api/v1/chat/health
├── service/
│   └── ChatService.java                # Business logic, model selection
├── client/
│   └── OllamaClient.java               # Reactive HTTP client for Ollama
├── model/
│   ├── ChatRequest.java                # Inbound DTO  (prompt, optional model)
│   ├── ChatResponse.java               # Outbound DTO (reply, durationMs)
│   └── OllamaRequest.java              # Ollama API payload
└── handlers/
    └── GlobalExceptionHandler.java     # Centralized error responses
```

## Endpoints

| Method | Route | Description |
|--------|-------|-------------|
| `GET` | `/api/v1/chat/health` | Health check — returns `{ "status": "UP" }` |
| `POST` | `/api/v1/chat` | Send a prompt, receive an AI response |

### POST /api/v1/chat

**Request**
```json
{
  "prompt": "Explain dependency injection in one sentence.",
  "model": "llama3"
}
```
> `model` is optional — falls back to `ai.engine.model` in `application.properties`.

**Response**
```json
{
  "reply": "Dependency injection is a design pattern...",
  "durationMs": 1234
}
```

## Configuration

`src/main/resources/application.properties`:

```properties
server.port=8080
ai.engine.base-url=http://localhost:11434
ai.engine.model=llama3
ai.engine.timeout-seconds=30
```

## Simulation Mode

`OllamaClient` has a `SIMULATE` flag (default `true`) that returns a canned response so you can run the app without a real Ollama server:

```java
// OllamaClient.java
private static final boolean SIMULATE = true;  // ← set false for real Ollama
```

## Prerequisites

- JDK 17+
- Maven
- (Optional) [Ollama](https://ollama.com) running locally with a model pulled, e.g.:
  ```bash
  ollama pull llama3
  ```

## Build & Run

```bash
# Run with Maven wrapper
./mvnw spring-boot:run

# Or build and run the JAR
./mvnw clean package
java -jar target/springboot-gateway-0.0.1-SNAPSHOT.jar
```

## Tech Stack

![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?logo=springboot&logoColor=white)
![WebFlux](https://img.shields.io/badge/WebFlux-reactive-6DB33F?logo=spring&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?logo=lombok&logoColor=white)
![Jakarta Validation](https://img.shields.io/badge/Jakarta_Validation-3.0-4B8BBE?logo=jakartaee&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
