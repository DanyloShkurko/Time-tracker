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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private TaskRepository taskRepository;

    /* ========================================================================================= */
    /* ================================= CREATE EMPLOYEE TESTS ================================= */
    /* ========================================================================================= */
    @Test
    public void testCreateEmployee() {
        EmployeeRequest employeeRequest = new EmployeeRequest("Name", "email@example.com");
        when(employeeRepository.save(any(Employee.class))).thenReturn(new Employee());

        String employeeId = employeeService.createEmployee(employeeRequest);

        assertNotNull(employeeId);
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

        EmployeeRequest employeeRequest = new EmployeeRequest("newName", "newEmail");

        when(employeeRepository.findById("1")).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);

        EmployeeResponse actualResponse = employeeService.editEmployeeInfo("1", employeeRequest);

        assertEquals("1", actualResponse.getId());
        assertEquals("newName", actualResponse.getEmployee_name());
        assertEquals("newEmail", actualResponse.getEmail());
    }


    @Test
    void editEmployeeInfo_FailureScenario() {
        when(employeeRepository.findById("non-existent-id")).thenReturn(Optional.empty());

        EmployeeRequest employeeRequest = new EmployeeRequest("newName", "newEmail");

        assertThrows(EntityNotFoundException.class, () -> employeeService.editEmployeeInfo("non-existent-id", employeeRequest));
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

        when(employeeRepository.findById(employee)).thenReturn(Optional.of(new Employee()));

        when(taskRepository.findTasksByEmployeeAndEndTimeBetween(eq(employee), eq(start), eq(end)))
                .thenReturn(List.of(
                        new Task("1", employee, "test", Instant.parse("2024-02-01T10:00:00Z"), Instant.parse("2024-02-01T11:00:00Z")),
                        new Task("2", employee, "test", Instant.parse("2024-02-02T10:00:00Z"), Instant.parse("2024-02-02T12:00:00Z")),
                        new Task("3", employee, "test", Instant.parse("2024-02-03T10:00:00Z"), Instant.parse("2024-02-03T10:30:00Z"))
                ));


        List<TaskResponse> actual = employeeService.showEmployeeEfforts(employee, start, end);

        assertEquals("2", actual.get(0).getId());
        assertEquals("1", actual.get(1).getId());
        assertEquals("3", actual.get(2).getId());
    }

    @Test
    void showEmployeeEfforts_FailureScenario() {
        String employeeId = "1";
        Instant start = LocalDate.parse("2024-02-01").atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.parse("2024-03-10").atStartOfDay(ZoneId.systemDefault()).toInstant();

        assertThrows(EntityNotFoundException.class, () -> employeeService.showEmployeeEfforts(employeeId, start, end));
    }


    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */

    /* ======================================================================================== */
    /* ========================== SHOW SUM OF EMPLOYEE EFFORTS TESTS ========================== */
    /* ======================================================================================== */

    @Test
    void showTheAmountOfLaborCostsForAllEmployeeTasks_SuccessScenario() {
        String employeeId = "1";
        Instant start = LocalDate.parse("2024-02-01").atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.parse("2024-03-10").atStartOfDay(ZoneId.systemDefault()).toInstant();

        when(employeeRepository.findById(eq(employeeId))).thenReturn(Optional.of(new Employee()));

        when(taskRepository.findTasksByEmployeeAndEndTimeBetween(eq(employeeId), eq(start), eq(end)))
                .thenReturn(List.of(
                        new Task("1", employeeId, "test", Instant.parse("2024-02-01T10:00:00Z"), Instant.parse("2024-02-01T11:00:00Z")),
                        new Task("2", employeeId, "test", Instant.parse("2024-02-02T10:00:00Z"), Instant.parse("2024-02-02T12:00:00Z")),
                        new Task("3", employeeId, "test", Instant.parse("2024-02-03T10:00:00Z"), Instant.parse("2024-02-03T10:30:00Z"))
                ));

        String expected = "03:30";
        String actual = employeeService.showTheAmountOfLaborCostsForAllEmployeeTasks(employeeId, start, end);

        assertEquals(expected, actual);
    }

    @Test
    void showTheAmountOfLaborCostsForAllEmployeeTasks_FailureScenario() {
        String employeeId = "1";
        Instant start = LocalDate.parse("2024-02-01").atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.parse("2024-03-10").atStartOfDay(ZoneId.systemDefault()).toInstant();

        assertThrows(EntityNotFoundException.class, () -> employeeService.showTheAmountOfLaborCostsForAllEmployeeTasks(employeeId, start, end));
    }

    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */

    /* ======================================================================================== */
    /* ============================ REMOVE ALL EMPLOYEE DATA TESTS ============================ */
    /* ======================================================================================== */
    @Test
    void removeEmployee_SuccessScenario() {
        String employeeId = UUID.randomUUID().toString();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee()));
        employeeService.removeEmployee(employeeId);
        verify(employeeRepository).deleteById(employeeId);
        assertDoesNotThrow(() -> employeeService.removeEmployee(employeeId));
    }

    @Test
    void removeEmployee_FailureScenario() {
        String employeeId = UUID.randomUUID().toString();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> employeeService.removeEmployee(employeeId));
    }
    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */
}