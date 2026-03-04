# Evidence Week 1 — Servlets, JSP & MVC with Tomcat

Maven web application covering **Parts 1–4** of the Java Web Environment exercise: environment setup, Servlets, JSP/JSTL, and MVC architecture. The Spring Framework & IoC portion (Part 5) lives in [Evidence_Week1_SpringBoot](../Evidence_Week1_SpringBoot/).

## What It Covers

### Servlets & Lifecycle (Part 2)

`HelloController` extends `HttpServlet` and demonstrates the full servlet lifecycle:

| Lifecycle Method | When It Runs |
|-----------------|--------------|
| `init()` | Once, when the servlet is first loaded |
| `service()` | Every request — routes by URL path |
| `destroy()` | Once, when the servlet is unloaded |

### JSP & JSTL (Part 3)

`hello.jsp` uses the Jakarta JSTL core taglib (`<c:forEach>`) to iterate over a list and render dynamic HTML — the **View** layer in MVC.

### MVC Architecture (Part 4)

| Role | Class/File | Responsibility |
|------|-----------|---------------|
| Model | `MessageModel` | Simple POJO carrying data |
| View | `*.jsp` pages | Renders the UI |
| Controller | `HelloController` | Receives requests, builds the model, forwards to view |

### Endpoints

| Route | Behavior |
|-------|----------|
| `GET /hello` | Direct HTML response from the servlet |
| `GET /hello-mvc` | MVC flow: controller → model → JSP view |

## Project Structure

```
src/main/
├── java/net/daifo/servlet/
│   ├── controllers/
│   │   └── HelloController.java   # HttpServlet (Controller)
│   ├── models/
│   │   └── MessageModel.java      # POJO (Model)
│   └── view/                      # View logic handled by JSPs
└── webapp/
    ├── WEB-INF/
    │   └── web.xml                # Servlet URL mappings
    ├── index.jsp                  # Landing page
    └── hello.jsp                  # JSP + JSTL demo (View)
```

## Prerequisites

- JDK 17+
- Apache Tomcat 10+ (Jakarta EE compatible)
- Maven
- VS Code extensions (optional):
  - Extension Pack for Java
  - Debugger for Java
  - Maven for Java
  - Tomcat for Visual Studio Code

## Build & Run

```bash
# Package the WAR
mvn clean package

# Deploy target/Evidence_Week_1.war to Tomcat manually,
# or right-click the WAR in VS Code → Run on Tomcat Server
```

Then open:

- `http://localhost:8080/Evidence_Week_1/hello` — direct servlet response
- `http://localhost:8080/Evidence_Week_1/hello-mvc` — full MVC flow

## Tech Stack

![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Jakarta EE](https://img.shields.io/badge/Jakarta_EE-Servlet_6.1-4B8BBE?logo=jakartaee&logoColor=white)
![JSTL](https://img.shields.io/badge/JSTL-3.0-4B8BBE?logo=jakartaee&logoColor=white)
![Tomcat](https://img.shields.io/badge/Tomcat-10-F8DC75?logo=apachetomcat&logoColor=black)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
