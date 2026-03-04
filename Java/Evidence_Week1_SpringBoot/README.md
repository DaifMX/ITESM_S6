# Evidence Week 1 — Spring Boot & IoC

Spring Boot application covering **Part 5** of the Java Web Environment exercise: Spring Framework & Inversion of Control. Parts 1–4 (Servlets, JSP, MVC with Tomcat) live in [Evidence_Week1_Tomcat](../Evidence_Week1_Tomcat/).

## What It Covers

### Spring Boot vs. Tomcat (Part 5)

Replaces manual Tomcat deployment + Servlet boilerplate with Spring Boot's embedded server and auto-configuration. No `web.xml`, no WAR packaging — just run the JAR.

### Inversion of Control & Dependency Injection

`MessageService` is annotated with `@Service`, registering it as a Spring-managed bean. `HelloController` receives it via **constructor injection** — Spring resolves and injects the dependency at startup automatically.

| Annotation | Purpose |
|------------|---------|
| `@SpringBootApplication` | Enables auto-configuration and component scanning |
| `@RestController` | Marks the class as a REST endpoint |
| `@GetMapping` | Maps HTTP GET requests to a handler method |
| `@Service` | Registers the class as a Spring bean |

### Endpoint

| Route | Behavior |
|-------|----------|
| `GET /hello` | Returns a plain-text greeting served by `MessageService` |

## Project Structure

```
src/main/java/net/daifo/evidence_week1_springboot/
├── EvidenceWeek1SpringBootApplication.java   # Entry point (@SpringBootApplication)
├── controllers/
│   └── HelloController.java                  # REST controller (constructor injection)
└── services/
    └── MessageService.java                   # @Service bean (IoC demo)
```

## Prerequisites

- JDK 17+
- Maven
- VS Code extensions (optional):
  - Extension Pack for Java
  - Spring Boot Extension Pack

## Build & Run

```bash
# Run directly via Maven wrapper
./mvnw spring-boot:run

# Or build the JAR and run it
./mvnw clean package
java -jar target/Evidence_Week1_SpringBoot-0.0.1-SNAPSHOT.jar
```

Then open: `http://localhost:8080/hello`

## Tech Stack

![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?logo=springboot&logoColor=white)
![Spring MVC](https://img.shields.io/badge/Spring_MVC-6DB33F?logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
