# Software Requirements — E1

Use case diagrams for a task-management web system with role-based access. Each functional requirement is authored in Mermaid (`.mmd`) and exported as an SVG (`.svg`).

## Functional Requirements

| Diagram | Requirement | Description |
|---------|-------------|-------------|
| [rf-001](rf-001.mmd) | RF-001 | Secure user authentication with role-based redirect |
| [rf-002](rf-002.mmd) | RF-002 | — |
| [rf-003](rf-003.mmd) | RF-003 | — |
| [rf-004](rf-004.mmd) | RF-004 | — |
| [rf-005](rf-005.mmd) | RF-005 | Developer dashboard — task view, KPI graphs, status updates |
| [rf-006](rf-006.mmd) | RF-006 | — |
| [rf-007](rf-007.mmd) | RF-007 | — |
| [rf-008](rf-008.mmd) | RF-008 | — |
| [rf-009](rf-009.mmd) | RF-009 | — |
| [rf-010](rf-010.mmd) | RF-010 | — |

## System Actors

| Actor | Role |
|-------|------|
| Manager | Oversees teams and task assignments |
| Developer | Views and updates assigned tasks |
| Administrator | Manages accounts and receives security alerts |
| Auth System | Validates credentials and enforces HTTPS |
| Oracle Autonomous DB | Persists all task and user data |
| Notification System | Delivers alerts for deadlines and status changes |

## Rendering Diagrams

Open any `.mmd` file in the [Mermaid Live Editor](https://mermaid.live), or use the VS Code Mermaid extension. GitHub renders `.mmd` natively inside Markdown code blocks tagged `mermaid`.
