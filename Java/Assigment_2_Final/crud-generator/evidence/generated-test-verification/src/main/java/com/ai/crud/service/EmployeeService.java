package com.ai.crud.service;

import com.ai.crud.entity.Employee;
import com.ai.crud.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// === AI-GENERATED (qwen2.5:3b) — verbatim. ===
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Transactional
    public Employee findEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }
}
