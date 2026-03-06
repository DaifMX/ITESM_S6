# TutorBot — Exercise & Student REST API

Spring Boot REST API that manages students and multiple-choice exercises. Students can submit answers and receive instant feedback with a score.

## Architecture

```
Client
  │
  ▼
StudentController  (/api/student)
ExerciseController (/api/exercise)
  │
  ▼
StudentService / ExerciseService   (validation, answer grading)
  │
  ▼
StudentRepository / ExerciseRepository   (in-memory store)
```

## Project Structure

```
src/main/java/net/daifo/tutorbot/
├── TutorbotApplication.java
├── controllers/
│   ├── StudentController.java     # POST /api/student  |  GET /api/student  |  GET /api/student/{id}
│   └── ExerciseController.java    # GET /api/exercise  |  POST /api/exercise/submit
├── services/
│   ├── StudentService.java        # Student CRUD logic
│   └── ExerciseService.java       # Difficulty filter + answer grading
├── models/
│   ├── StudentModel.java          # UUID id, name
│   └── ExerciseModel.java         # UUID id, topic, question, options A/B/C, answer, difficulty
├── dtos/
│   ├── AnswerRequest.java         # { studentId, exerciseId, answer }
│   └── FeedbackResponse.java      # { studentId, exerciseId, answer, score, message, correct }
└── repositories/
    ├── StudentRepository.java
    └── ExerciseRepository.java
```

## Endpoints

### Students

| Method | Route | Description |
|--------|-------|-------------|
| `POST` | `/api/student` | Register a new student |
| `GET` | `/api/student` | List all students |
| `GET` | `/api/student/{id}` | Get student by UUID |

**POST /api/student — Request**
```json
{
  "name": "John Doe"
}
```

**Response** `201 Created`
```json
{
  "id": "a1b2c3d4-...",
  "name": "John Doe"
}
```

---

### Exercises

| Method | Route | Description |
|--------|-------|-------------|
| `GET` | `/api/exercise` | List all exercises (optional `?difficulty=easy\|medium\|hard`) |
| `POST` | `/api/exercise/submit` | Submit an answer and get feedback |

**GET /api/exercise?difficulty=easy — Response** `200 OK`
```json
[
  {
    "id": "e1e2e3e4-...",
    "topic": "OOP",
    "question": "What does encapsulation mean?",
    "optionA": "Hiding data",
    "optionB": "Sharing data",
    "optionC": "Deleting data",
    "difficulty": "easy"
  }
]
```
> `answer` is **not** returned to the client on GET — only on feedback after submission.

**POST /api/exercise/submit — Request**
```json
{
  "studentId": "a1b2c3d4-...",
  "exerciseId": "e1e2e3e4-...",
  "answer": "A"
}
```

**Response** `200 OK`
```json
{
  "studentId": "a1b2c3d4-...",
  "exerciseId": "e1e2e3e4-...",
  "answer": "A",
  "score": 100,
  "message": "Correct! Well done.",
  "correct": true
}
```

**Error responses** use RFC 9457 `ProblemDetail`:
```json
{
  "type": "about:blank",
  "status": 404,
  "detail": "Exercise with id: <uuid> does not exist"
}
```

## Answer Validation

- Accepted values: `A`, `B`, `C` (case-insensitive — normalized to uppercase)
- Score: `100` if correct, `0` if incorrect
- Both `studentId` and `exerciseId` must exist, otherwise `404`

## Difficulty Filter

Valid values: `easy` | `medium` | `hard`
Passing any other value returns `400 Bad Request`.

## Prerequisites

- JDK 17+
- Maven

## Build & Run

```bash
# Run with Maven wrapper
./mvnw spring-boot:run

# Or build and run the JAR
./mvnw clean package
java -jar target/tutorbot-0.0.1-SNAPSHOT.jar
```

API will be available at `http://localhost:8080`.

## Tech Stack

![Java 17](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?logo=springboot&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?logo=lombok&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)
