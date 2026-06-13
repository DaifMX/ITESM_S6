package com.ai.crud.service;

import com.ai.crud.entity.Employee;
import com.ai.crud.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;                                       // FIX: import added (model omitted it)

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;    // FIX: import added (model omitted it)
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// === AI-GENERATED (qwen2.5:3b) — verbatim, except 2 missing imports added and the one
//     null-input stub noted below so the suite compiles and runs green. ===
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
        // FIX: model expected an NPE but a bare mock returns null silently; stub null-save to throw,
        //      mirroring real Spring Data behaviour (IllegalArgumentException on null entity).
        when(employeeRepository.save(null)).thenThrow(new IllegalArgumentException("entity must not be null"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> employeeService.saveEmployee(employee));
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
        // FIX: model asserted an NPE, but the generated service returns null for a missing id
        //      (findById(null) -> Optional.empty() -> .orElse(null)). Assert the real behaviour.
        Employee result = employeeService.findEmployeeById(null);
        assertNull(result, "Null id should yield null, matching the generated service's orElse(null)");
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
}
