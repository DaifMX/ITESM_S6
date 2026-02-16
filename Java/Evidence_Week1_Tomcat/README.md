# Evidence Week 1 - Java Servlets, JSP & MVC with Tomcat

Maven web application covering Parts 1-4 of the Java Web Environment exercise: environment setup, Servlets, JSP/JSTL, and MVC architecture. The Spring Framework & IoC portion (Part 5) lives in a separate project within this monorepo.

## Prerequisites

- JDK 17+
- Apache Tomcat 10+ (Jakarta EE compatible)
- Maven
- VS Code with the following extensions:
  - Extension Pack for Java
  - Debugger for Java
  - Maven for Java
  - Tomcat for Visual Studio Code

## Project Structure

```
src/main/
├── java/net/daifo/servlet/
│   ├── controllers/
│   │   └── HelloController.java   # Servlet (Controller)
│   ├── models/
│   │   └── MessageModel.java      # POJO (Model)
│   └── view/                      # (View logic handled by JSPs)
└── webapp/
    ├── WEB-INF/
    │   └── web.xml                # Servlet mappings
    ├── index.jsp                  # Landing page
    └── hello.jsp                  # JSP with JSTL demo
```

## What It Covers

### Servlets & Lifecycle (Part 2)

`HelloController` extends `HttpServlet` and demonstrates the full servlet lifecycle:

- **`init()`** - called once when the servlet is first loaded
- **`service()`** - handles every incoming request, routing by path
- **`destroy()`** - called when the servlet is taken out of service

Mapped to two URL patterns in `web.xml`:

| Route        | Behavior                                  |
|--------------|-------------------------------------------|
| `/hello`     | Returns a plain HTML response from the servlet |
| `/hello-mvc` | Forwards to `hello.jsp` via the MVC pattern   |

### JSP & JSTL (Part 3)

`hello.jsp` uses the Jakarta JSTL core taglib (`<c:forEach>`) to iterate over a list and render dynamic content, acting as the **View** layer.

### MVC Architecture (Part 4)

The project is organized following the Model-View-Controller pattern:

- **Model** - `MessageModel`: simple POJO carrying data
- **View** - JSP pages rendering the UI
- **Controller** - `HelloController`: receives requests, creates model objects, and forwards to the view

## Build & Run

```bash
# Package the WAR
mvn clean package

# Deploy target/Evidence_Week_1.war to Tomcat
# Or use the VS Code Tomcat extension: right-click the WAR -> Run on Tomcat Server
```

Then open:

- `http://localhost:8080/Evidence_Week_1/hello` - direct servlet response
- `http://localhost:8080/Evidence_Week_1/hello-mvc` - MVC flow through JSP

## Tech Stack

- Java 17
- Jakarta Servlet API 6.1
- Jakarta JSTL 3.0
- Maven
- Apache Tomcat 10+
