package com.example.Timetracker.service;

import com.example.Timetracker.entity.Employee;
import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testCreateEmployee() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Name", "email@example.com");
        String employeeId = employeeService.createEmployee(employeeRequest);

        Employee found = employeeRepository.findById(employeeId).orElse(null);

        assertNotNull(found);
        assertEquals(employeeRequest.getEmployee_name(), found.getEmployee_name());
        assertEquals(employeeRequest.getEmail(), found.getEmail());
    }

    @Test
    void editEmployeeInfo() {
    }

    @Test
    void showEmployeeEfforts() {
    }

    @Test
    void showTheAmountOfLaborCostsForAllEmployeeTasks() {
    }
}