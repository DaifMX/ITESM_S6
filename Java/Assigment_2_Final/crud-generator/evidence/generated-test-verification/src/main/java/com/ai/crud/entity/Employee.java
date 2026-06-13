package com.ai.crud.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

// === AI-GENERATED (qwen2.5:3b) — verbatim, except the getters/setters the model left as a
//     "// Getters and Setters" comment have been filled in so the entity is usable. ===
@Entity(name = "employees")
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    // Getters and Setters (filled in)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }
    public LocalDateTime getHireDate() { return hireDate; }
    public void setHireDate(LocalDateTime hireDate) { this.hireDate = hireDate; }
}
