package com.example.Timetracker.service;

import com.example.Timetracker.entity.Employee;
import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.repository.EmployeeRepository;
import com.example.Timetracker.utlil.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    /* ========================================================================================= */
    /* ================================= CREATE EMPLOYEE TESTS ================================= */
    /* ========================================================================================= */
    @Test
    public void testCreateEmployee() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Name", "email@example.com");
        String employeeId = employeeService.createEmployee(employeeRequest);

        Employee found = employeeRepository.findById(employeeId).orElse(null);

        assertNotNull(found);
        assertEquals(employeeRequest.getEmployee_name(), found.getEmployee_name());
        assertEquals(employeeRequest.getEmail(), found.getEmail());
    }

    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */

    /* ========================================================================================= */
    /* ================================== EDIT EMPLOYEE TESTS ================================== */
    /* ========================================================================================= */
    @Test
    void editEmployeeInfo_SuccessScenario() {
        Employee existingEmployee = Employee.builder()
                .id("1")
                .employee_name("oldName")
                .email("oldEmail")
                .build();

        employeeRepository.save(existingEmployee);

        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .employee_name("newName")
                .email("newEmail")
                .build();

        EmployeeResponse actualResponse = employeeService.editEmployeeInfo(existingEmployee.getId(), employeeRequest);

        assertEquals(existingEmployee.getId(), actualResponse.getId());
        assertEquals(employeeRequest.getEmployee_name(), actualResponse.getEmployee_name());
        assertEquals(employeeRequest.getEmail(), actualResponse.getEmail());
    }

    @Test
    void editEmployeeInfo_FailureScenario() {
        EmployeeRequest employeeRequest = EmployeeRequest.builder()
                .employee_name("newName")
                .email("newEmail")
                .build();

        assertThrows(EntityNotFoundException.class, () -> employeeService.editEmployeeInfo("1", employeeRequest));
    }

    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */


    @Test
    void showEmployeeEfforts() {
    }

    @Test
    void showTheAmountOfLaborCostsForAllEmployeeTasks() {
    }
}