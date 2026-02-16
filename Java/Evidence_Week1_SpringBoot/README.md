# Evidence Week 1 - Spring Boot & IoC

Spring Boot application covering Part 5 of the Java Web Environment exercise: Spring Framework & Inversion of Control. The Servlets, JSP, and MVC portions (Parts 1-4) live in a separate project within this monorepo.

## Prerequisites

- JDK 17+
- Maven
- VS Code with the following extensions:
  - Extension Pack for Java
  - Spring Boot Extension Pack

## Project Structure

```
src/main/java/net/daifo/evidence_week1_springboot/
├── EvidenceWeek1SpringBootApplication.java   # Entry point
├── controllers/
│   └── HelloController.java                  # REST controller
└── services/
    └── MessageService.java                   # Service bean (IoC)
```

## What It Covers

### Spring Boot Setup (Part 5)

Replaces the manual Tomcat + Servlet approach with Spring Boot's embedded server and auto-configuration.

### Inversion of Control & Dependency Injection

`MessageService` is annotated with `@Service`, making it a Spring-managed bean. `HelloController` receives it via **constructor injection** — Spring automatically resolves and injects the dependency at startup.

| Annotation        | Purpose                                      |
|-------------------|----------------------------------------------|
| `@SpringBootApplication` | Enables auto-configuration and component scanning |
| `@RestController`        | Marks the class as a REST endpoint                |
| `@GetMapping`            | Maps HTTP GET requests to a handler method        |
| `@Service`               | Registers the class as a Spring bean              |

### Endpoint

| Route    | Behavior                                          |
|----------|---------------------------------------------------|
| `/hello` | Returns a plain-text greeting from `MessageService` |

## Build & Run

```bash
# Run directly
./mvnw spring-boot:run

# Or package and run the JAR
./mvnw clean package
java -jar target/Evidence_Week1_SpringBoot-0.0.1-SNAPSHOT.jar
```

Then open: `http://localhost:8080/hello`

## Tech Stack

- Java 17
- Spring Boot 4.0
- Spring Web MVC
- Maven
