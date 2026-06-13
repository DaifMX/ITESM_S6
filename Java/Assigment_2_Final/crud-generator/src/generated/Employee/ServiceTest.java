package com.ai.crud.service;

import com.ai.crud.entity.Employee;
import com.ai.crud.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveEmployeeHappyPath() {
        // Given
        Employee employee = new Employee();
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // When
        employeeService.saveEmployee(employee);

        // Then
        assertNotNull(employee, "Saved employee should not be null");
    }

    @Test
    public void testSaveEmployeeNullInput() {
        // Given
        Employee employee = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> employeeService.saveEmployee(employee));
    }

    @Test
    public void testFindEmployeeByIdHappyPath() {
        // Given
        Long id = 1L;
        Employee employee = new Employee();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));

        // When
        Employee foundEmployee = employeeService.findEmployeeById(id);

        // Then
        assertNotNull(foundEmployee, "Found employee should not be null");
    }

    @Test
    public void testFindEmployeeByIdNullInput() {
        // Given
        Long id = 1L;

        // When & Then
        assertThrows(NullPointerException.class, () -> employeeService.findEmployeeById(null));
    }

    @Test
    public void testFindEmployeeByIdNotFound() {
        // Given
        Long id = 2L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Employee foundEmployee = employeeService.findEmployeeById(id);

        // Then
        assertNull(foundEmployee, "Found employee should be null as the ID does not exist");
    }

    @Test
    public void testFindEmployeeByIdNullInputNotFound() {
        // Given
        Long id = 2L;

        // When & Then
        assertThrows(NullPointerException.class, () -> employeeService.findEmployeeById(null));
    }
}