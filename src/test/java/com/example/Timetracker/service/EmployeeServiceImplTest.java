package com.example.Timetracker.service;

import com.example.Timetracker.entity.Employee;
import com.example.Timetracker.entity.Task;
import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.model.TaskResponse;
import com.example.Timetracker.repository.EmployeeRepository;
import com.example.Timetracker.repository.TaskRepository;
import com.example.Timetracker.utlil.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @MockBean
    private TaskRepository taskRepository;

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

    /* ========================================================================================= */
    /* ============================== SHOW EMPLOYEE EFFORTS TESTS ============================== */
    /* ========================================================================================= */

    @Test
    void showEmployeeEfforts_SuccessScenario() {
        String employee = "0ec35bbc-132b-43c5-a235-e61b2159acbf";
        Instant start = LocalDate.parse("2024-02-01").atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.parse("2024-03-10").atStartOfDay(ZoneId.systemDefault()).toInstant();
        when(taskRepository.findTasksByEmployeeAndEndTimeBetween(eq(employee), eq(start), eq(end)))
                .thenReturn(List.of(
                        new Task("1", employee, "test", Instant.parse("2024-02-01T10:00:00Z"), Instant.parse("2024-02-01T11:00:00Z")),
                        new Task("2", employee, "test", Instant.parse("2024-02-02T10:00:00Z"), Instant.parse("2024-02-02T12:00:00Z")),
                        new Task("3", employee,"test", Instant.parse("2024-02-03T10:00:00Z"), Instant.parse("2024-02-03T10:30:00Z"))
                ));


        List<TaskResponse> actual = employeeService.showEmployeeEfforts(employee, start, end);

        assertEquals("2", actual.get(0).getId());
        assertEquals("1", actual.get(1).getId());
        assertEquals("3", actual.get(2).getId());
    }


    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */

    @Test
    void showTheAmountOfLaborCostsForAllEmployeeTasks() {
    }
}