package com.ai.crud.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

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

    // Getters and Setters
}