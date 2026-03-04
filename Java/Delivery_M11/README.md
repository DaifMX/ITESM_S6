# Delivery M11 — Student Record System

Console application for registering and analyzing student performance. Covers all core Java concepts from Weeks 1–5: fundamentals, methods, arrays & collections, and OOP inheritance.

## Features

- Multi-classroom support — create, switch between, and manage independent classrooms
- Register students as **Regular** or **Scholarship** type
- Assign grades (validated 0.0–10.0) per student
- View a formatted grade table sorted by grade (highest → lowest)
- Statistics: average, highest, lowest, pass count, pass rate
- Search a student by name and display their grade + status
- Recursive grade summation used internally by the average calculation

## Project Structure

```
src/main/java/net/daifo/
├── Main.java                          # Entry point & main menu loop
├── models/
│   ├── PersonModel.java               # Base class (name, age)
│   ├── StudentModel.java              # Extends Person (UUID id, getType)
│   ├── RegularStudentModel.java       # Overrides getType → "Regular"
│   ├── ScholarshipStudentModel.java   # Overrides getType → "Scholarship"
│   └── ClassroomModel.java            # Holds ArrayList<Student> + HashMap<id, grade>
└── services/
    ├── MenuService.java               # All menu action handlers
    └── StatsService.java              # Pure stat methods (avg, min, max, count, recursive sum)
```

## Class Hierarchy

```
PersonModel
└── StudentModel
    ├── RegularStudentModel
    └── ScholarshipStudentModel
```

## Key Methods — StatsService

| Method | Description |
|--------|-------------|
| `calculateAverage(double[])` | Group average — delegates to `sumGrades` |
| `sumGrades(double[], int i)` | **Recursive** — sums grades[0..i] |
| `getStatus(double)` | Returns `"Passed"` (≥ 6.0) or `"Failed"` |
| `highestGrade(double[])` | Maximum grade in the array |
| `lowestGrade(double[])` | Minimum grade in the array |
| `countPassed(double[])` | Count of grades ≥ 6.0 |

## Collections Used

| Structure | Purpose |
|-----------|---------|
| `double[]` | Grade array for stats calculations |
| `ArrayList<StudentModel>` | Ordered student roster per classroom |
| `HashMap<String, Double>` | Maps student UUID → grade for O(1) lookup & search |

## Prerequisites

- JDK 21+
- Maven
- Any IDE with Java support (IntelliJ IDEA recommended)

## Build & Run

```bash
mvn compile exec:java -Dexec.mainClass="net.daifo.Main"
```

Or from IntelliJ: run `Main.java` directly.

## Menu

```
╔═══════════════════════╗
║  STUDENT SYSTEM v1.0  ║
╚═══════════════════════╝

Pick an option:
    1) Register students
    2) Register grades
    3) View grades
    4) View statistics
    5) Search students
    6) Switch classroom
    0) Exit
```

## Tech Stack

![Java 21](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
