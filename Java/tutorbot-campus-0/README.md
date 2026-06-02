# TutorBot Campus

An AI-powered university tutoring platform built with a microservices architecture. The platform automates the student tutoring workflow — from evaluating answers with a local LLM to detecting learning gaps and generating personalized remediation paths.

## What It Does

1. **Evaluates student answers** — The Evaluator Service scores answers using an Ollama-hosted LLM (llama3) and persists results to Oracle DB.
2. **Detects learning gaps** — The Gap Detector Service analyzes evaluation results to identify weak mastery areas.
3. **Generates learning paths** _(planned)_ — A recommender service will generate remediation paths based on detected gaps.
4. **Notifies students** _(planned)_ — A notifier service will alert students of new learning opportunities.

Services are fully decoupled and communicate asynchronously via RabbitMQ events.

## Architecture

```
Student Answer
      │
      ▼
[Evaluator Service]  ──── evaluation.completed ────▶  [Gap Detector Service]
      │                                                        │
   Oracle DB                                               Oracle DB
                                                               │
                                                        gap.detected
                                                               │
                                                               ▼
                                                  [Learning Path Service] (planned)
                                                               │
                                                        path.updated
                                                               │
                                                               ▼
                                                  [Notifier Service] (planned)
```

All services register with a **Eureka** service discovery server and are exposed through an **API Gateway** in production.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.2 |
| Service Discovery | Spring Cloud Netflix Eureka |
| Fault Tolerance | Resilience4j (circuit breakers) |
| Messaging | RabbitMQ 3.13 |
| Database | Oracle XE 21 |
| Caching | Redis 7.2 |
| Search | Apache Solr 9.6 |
| Document Store | MongoDB 7.0 |
| AI / LLM | Ollama (llama3, local) |
| Observability | ELK Stack 8.14.3 (full stack only) |
| Containerization | Docker & Docker Compose |
| Build | Maven 3.9.8 |

## Project Structure

```
tutorbot-campus/
├── backend/
│   ├── evaluator-service/      # Scores student answers via Ollama AI
│   └── gap-detector-service/   # Detects learning gaps from evaluations
└── infrastructure/
    ├── docker-compose.dev.yml  # Core services (DB, MQ, cache, Ollama)
    ├── docker-compose.full.yml # Full stack (all services + ELK)
    ├── oracle/                 # Oracle DB initialization scripts
    └── postman/                # API testing collections
```

## Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 21 JDK
- Maven 3.9.8+

### 1. Start Infrastructure

```bash
cd infrastructure
docker compose -f docker-compose.dev.yml up -d
```

This starts: Oracle XE, RabbitMQ, Redis, MongoDB, Solr, and Ollama.

| Service | Port |
|---|---|
| Oracle DB | 1522 |
| RabbitMQ | 5672 / 15672 (UI) |
| Redis | 6379 |
| MongoDB | 27017 |
| Solr | 8983 |
| Ollama | 11434 |

### 2. Run a Service Locally

```bash
cd backend/evaluator-service
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

```bash
cd backend/gap-detector-service
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

| Service | Port |
|---|---|
| Evaluator Service | 8082 |
| Gap Detector Service | 8084 |

### 3. Full Stack (Optional)

```bash
cd infrastructure
docker compose -f docker-compose.full.yml up -d
```

Adds the Eureka server, API Gateway, all backend services as containers, and the ELK stack.

## Configuration

Each service has three property files:

| File | Profile | Purpose |
|---|---|---|
| `application.properties` | default / docker | Production defaults |
| `application-dev.properties` | dev | Local overrides (Eureka and RabbitMQ disabled) |

Key settings to be aware of:

```properties
# LLM
ollama.base-url=http://localhost:11434
ollama.model=llama3

# Messaging — set to true to enable RabbitMQ locally
tutorbot.messaging.enabled=false

# Database (dev)
spring.datasource.url=jdbc:oracle:thin:@localhost:1522/TESTDB
spring.datasource.username=Adolfo
spring.datasource.password=password
```

> Oracle credentials are for local development only. Do not use in production without changing the defaults.

## Event Contract

| Event | Published By | Consumed By |
|---|---|---|
| `student.answered` | Tutorial UI | Evaluator Service |
| `evaluation.completed` | Evaluator Service | Gap Detector, Recommender |
| `gap.detected` | Gap Detector Service | Learning Path, Notifier |
| `path.updated` | Learning Path Service | Notifier |

## Maven Commands

Run from within any service directory:

```bash
./mvnw clean package          # Build JAR
./mvnw test                   # Run tests
./mvnw spring-boot:run        # Run the application
```
