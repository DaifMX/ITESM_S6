# Advanced Databases

Database design, Oracle SQL, and schema modeling for a task management system backed by Oracle Autonomous DB.

## Contents

| Item | Description |
|------|-------------|
| [queries-v1.sql](queries-v1.sql) | DDL and DML — creates `Employee`, `Team`, `Task`, `EmployeeTeam`, and `EmployeeTask` tables and seeds sample data |
| [diagram.mmd](diagram.mmd) | Entity-relationship diagram of the full schema |
| [04-03-2025/](04-03-2025/) | Step-by-step setup diagrams and Oracle Telegram bot proof screenshots |

## Schema Overview

```
Employee ──< EmployeeTeam >── Team
Employee ──< EmployeeTask >── Task
```

| Table | Purpose |
|-------|---------|
| `Employee` | Stores employee details — name, modality (Remote / Hybrid / Onsite), position, phone |
| `Team` | Groups employees into teams |
| `Task` | Tracks tasks with start date, expected end date, and completion date |
| `EmployeeTeam` | Many-to-many: employees ↔ teams |
| `EmployeeTask` | Many-to-many: employees ↔ tasks |

## Topics Covered

- Oracle SQL DDL (CREATE TABLE, PRIMARY KEY, FOREIGN KEY)
- Many-to-many join tables
- Date handling with `TO_DATE`
- `CLOB` columns for long text
- Mermaid ER diagrams for visual schema documentation
