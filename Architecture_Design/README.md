# Architecture Design

Software architecture coursework — GoF design patterns implemented in Java and layered architecture diagrams for a live-streaming platform.

## Activities

| Activity | Description | Stack |
|----------|-------------|-------|
| [Activity-6](Activity-6/) | Layered and subroutine architecture diagrams for a live-streaming platform | Mermaid |
| [act-8-creational-design-patterns](act-8-creational-desgin-patterns/) | Singleton, Builder, Factory Method, Abstract Factory, Prototype | ![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white) |
| [act9-structural-design-patterns](act9-structural-design-patterns/) | Adapter, Bridge, Composite, Decorator, Facade, Flyweight, Proxy | ![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white) |
| [act11-behavioral-design-patterns](act11-behavioral-design-patterns/) | Chain of Responsibility, Command, Iterator, Mediator, Memento, Observer, State, Strategy, Template Method, Visitor | ![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white) ![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white) |

## Patterns at a Glance

### Creational
Control how objects are instantiated — decouple clients from concrete classes.

| Pattern | Intent |
|---------|--------|
| Singleton | One instance, global access point |
| Builder | Assemble complex objects step by step |
| Factory Method | Let subclasses decide which class to instantiate |
| Abstract Factory | Families of related objects without specifying concrete classes |
| Prototype | Clone existing objects instead of constructing from scratch |

### Structural
Compose objects and classes into larger structures.

| Pattern | Intent |
|---------|--------|
| Adapter | Bridge incompatible interfaces |
| Bridge | Decouple abstraction from implementation |
| Composite | Treat individual objects and compositions uniformly |
| Decorator | Add behavior to objects dynamically |
| Facade | Simplified interface to a subsystem |
| Flyweight | Share state to support many fine-grained objects efficiently |
| Proxy | Controlled access to another object |

### Behavioral
Define how objects communicate and distribute responsibility.

| Pattern | Intent |
|---------|--------|
| Chain of Responsibility | Pass a request along a chain of handlers |
| Command | Encapsulate a request as an object |
| Iterator | Sequentially access elements without exposing the underlying structure |
| Mediator | Centralize complex communication between objects |
| Memento | Capture and restore object state |
| Observer | Notify dependents automatically when state changes |
| State | Alter behavior when internal state changes |
| Strategy | Swap algorithms at runtime |
| Template Method | Define the skeleton of an algorithm, defer steps to subclasses |
| Visitor | Separate an algorithm from the object structure it operates on |

## Running the Pattern Demos

Each activity is a standalone Maven project. From any activity directory:

```bash
mvn compile exec:java
```
