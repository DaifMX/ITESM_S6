-- CREATE TABLE Employee
CREATE TABLE Employee (
    employee_id NUMBER(10,0) PRIMARY KEY,
    first_name VARCHAR2(50),
    last_name VARCHAR2(50),
    modality VARCHAR2(50),
    position VARCHAR2(50),
    phone_number VARCHAR2(20)
);

-- CREATE TABLE Team
CREATE TABLE Team (
    team_id NUMBER(10,0) PRIMARY KEY
);

-- CREATE TABLE Task - CLOB en lugar de TEXT
CREATE TABLE Task (
    task_id NUMBER(10,0) PRIMARY KEY,
    description CLOB,
    start_date DATE,
    end_date DATE,
    expected_end_date DATE
);

-- CREATE TABLE EmployeeTeam
CREATE TABLE EmployeeTeam (
    employee_id NUMBER(10,0),
    team_id NUMBER(10,0),
    PRIMARY KEY (employee_id, team_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
    FOREIGN KEY (team_id) REFERENCES Team(team_id)
);

-- CREATE TABLE EmployeeTask
CREATE TABLE EmployeeTask (
    employee_id NUMBER(10,0),
    task_id NUMBER(10,0),
    PRIMARY KEY (employee_id, task_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
    FOREIGN KEY (task_id) REFERENCES Task(task_id)
);

-- INSERT INTO Employee
INSERT INTO Employee (employee_id, first_name, last_name, modality, position, phone_number)
VALUES (1, 'Diego', 'Angulo', 'Remote', 'Developer', '5551234567');

INSERT INTO Employee (employee_id, first_name, last_name, modality, position, phone_number)
VALUES (2, 'Gael', 'Castillo', 'Hybrid', 'Data Analyst', '5559876543');

INSERT INTO Employee (employee_id, first_name, last_name, modality, position, phone_number)
VALUES (3, 'Eugenio', 'Loeza', 'Onsite', 'Backend Developer', '5552223333');

-- INSERT INTO Team
INSERT INTO Team (team_id) VALUES (101);
INSERT INTO Team (team_id) VALUES (102);

-- INSERT INTO Task
INSERT INTO Task (task_id, description, start_date, end_date, expected_end_date)
VALUES (201, 'Implement chatbot API', TO_DATE('2025-02-10', 'YYYY-MM-DD'), NULL, TO_DATE('2025-02-20', 'YYYY-MM-DD'));

INSERT INTO Task (task_id, description, start_date, end_date, expected_end_date)
VALUES (202, 'Design database schema', TO_DATE('2025-02-11', 'YYYY-MM-DD'), NULL, TO_DATE('2025-02-18', 'YYYY-MM-DD'));

-- INSERT INTO EmployeeTeam
INSERT INTO EmployeeTeam (employee_id, team_id) VALUES (1, 101);
INSERT INTO EmployeeTeam (employee_id, team_id) VALUES (2, 101);
INSERT INTO EmployeeTeam (employee_id, team_id) VALUES (3, 102);

-- INSERT INTO EmployeeTask
INSERT INTO EmployeeTask (employee_id, task_id) VALUES (1, 201);
INSERT INTO EmployeeTask (employee_id, task_id) VALUES (3, 201);
INSERT INTO EmployeeTask (employee_id, task_id) VALUES (2, 202);

-- Commit para persistir
COMMIT;