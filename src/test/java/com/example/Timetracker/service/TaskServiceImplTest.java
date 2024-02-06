package com.example.Timetracker.service;

import com.example.Timetracker.entity.Employee;
import com.example.Timetracker.entity.Task;
import com.example.Timetracker.model.TaskRequest;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    /* ========================================================================================= */
    /* =================================== CREATE TASK TESTS =================================== */
    /* ========================================================================================= */

    @Test
    void createTask_SuccessScenario() {
        when(taskRepository.save(any(Task.class))).thenReturn(new Task());
        when(employeeRepository.findById(any())).thenReturn(Optional.of(new Employee()));

        assertNotNull(taskService.createTask(new TaskRequest()));
    }

    @Test
    void createTask_FailureScenario() {
        TaskRequest request = TaskRequest.builder()
                .employeeId("1")
                .name("task")
                .build();

        assertThrows(EntityNotFoundException.class, () -> taskService.createTask(request));
    }

    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */

    /* ======================================================================================== */
    /* ============================== START TASK COUNTDOWN TESTS ============================== */
    /* ======================================================================================== */

    @Test
    void startTaskCountdown_SuccessScenario() {
        String taskId = UUID.randomUUID().toString();

        Task task = Task.builder()
                .id(taskId)
                .name("task")
                .employee("1")
                .build();

        when(taskRepository.findById(eq(taskId))).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(new Task());

        taskService.startTaskCountdown(taskId);

        assertNotNull(task.getStartTime());
    }

    @Test
    void startTaskCountdown_FailureScenario() {
        assertThrows(EntityNotFoundException.class, () -> taskService.startTaskCountdown("-1"));
    }

    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */

    /* ======================================================================================== */
    /* ==================================== END TASK TESTS ==================================== */
    /* ======================================================================================== */

    @Test
    void stopTaskCountdown_SuccessScenario() {
        String taskId = UUID.randomUUID().toString();

        Task task = Task.builder()
                .id(taskId)
                .name("task")
                .employee("1")
                .build();

        when(taskRepository.findById(eq(taskId))).thenReturn(Optional.of(task));
        when(taskRepository.save(any())).thenReturn(new Task());

        taskService.stopTaskCountdown(taskId);

        assertNotNull(task.getEndTime());
    }

    @Test
    void stopTaskCountdown_FailureScenario() {
        assertThrows(EntityNotFoundException.class, () -> taskService.stopTaskCountdown("-1"));
    }

    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */

    /* ======================================================================================== */
    /* ===================== SHOW ALL WORK INTERVALS BY EMPLOYEE ID TESTS ===================== */
    /* ======================================================================================== */

    @Test
    void showAllWorkIntervalsByEmployeeId_SuccessScenario() {
        String employee = UUID.randomUUID().toString();

        String start = "2024-02-01";
        String end = "2024-03-10";

        Instant startInstant = LocalDate.parse(start).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = LocalDate.parse(end).atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Task> tasks = List.of(
                new Task("1", employee, "test", Instant.parse("2024-02-01T10:00:00Z"), Instant.parse("2024-02-01T11:00:00Z")),
                new Task("2", employee, "test", Instant.parse("2024-02-02T10:00:00Z"), Instant.parse("2024-02-02T12:00:00Z")),
                new Task("3", employee, "test", Instant.parse("2024-02-03T10:00:00Z"), Instant.parse("2024-02-03T10:30:00Z"))
        );

        when(employeeRepository.findById(eq(employee))).thenReturn(Optional.of(new Employee()));
        when(taskRepository.findTasksByEmployeeAndEndTimeBetween(eq(employee), eq(startInstant), eq(endInstant))).thenReturn(tasks);

        List<TaskResponse> actual = taskService.showAllWorkIntervalsByEmployeeId(employee, startInstant, endInstant);

        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i).getId(), tasks.get(i).getId());
            assertEquals(actual.get(i).getEmployee(), tasks.get(i).getEmployee());
            assertEquals(actual.get(i).getName(), tasks.get(i).getName());
            assertEquals(actual.get(i).getStart_time(), tasks.get(i).getStartTime());
            assertEquals(actual.get(i).getEnd_time(), tasks.get(i).getEndTime());
        }
    }

    @Test
    void showAllWorkIntervalsByEmployeeId_FailureScenario() {
        String employee = UUID.randomUUID().toString();

        String start = "2024-02-01";
        String end = "2024-03-10";

        Instant startInstant = LocalDate.parse(start).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = LocalDate.parse(end).atStartOfDay(ZoneId.systemDefault()).toInstant();

        assertThrows(EntityNotFoundException.class, () -> taskService.showAllWorkIntervalsByEmployeeId(employee, startInstant, endInstant));
    }

    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */

    /* ======================================================================================== */
    /* ========================== CLEAR EMPLOYEE TRACKING DATA TESTS ========================== */
    /* ======================================================================================== */

    @Test
    void clearEmployeeTrackingData_SuccessScenario() {
        String employeeId = UUID.randomUUID().toString();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee()));

        taskService.clearEmployeeTrackingData(employeeId);

        verify(taskRepository).deleteTasksByEmployee(employeeId);

        assertDoesNotThrow(() -> taskService.clearEmployeeTrackingData(employeeId));
    }

    @Test
    void clearEmployeeTrackingData_FailureScenario() {

    }
}