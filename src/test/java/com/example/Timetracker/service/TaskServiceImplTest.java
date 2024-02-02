package com.example.Timetracker.service;

import com.example.Timetracker.entity.Employee;
import com.example.Timetracker.entity.Task;
import com.example.Timetracker.model.TaskRequest;
import com.example.Timetracker.repository.EmployeeRepository;
import com.example.Timetracker.repository.TaskRepository;
import com.example.Timetracker.utlil.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /* ========================================================================================= */
    /* =================================== CREATE TASK TESTS =================================== */
    /* ========================================================================================= */

    @Test
    void createTask_SuccessScenario() {
        String employeeId = employeeRepository.save(Employee.builder()
                .id(UUID.randomUUID().toString())
                .employee_name("name")
                .email("email@example.com")
                .build()).getId();

        TaskRequest request = TaskRequest.builder()
                .employeeId(employeeId)
                .name("task")
                .build();

        String taskId = taskService.createTask(request).getId();

        Optional<Task> actual = taskRepository.findById(taskId);

        assertTrue(actual.isPresent());
        Task actualTask = actual.get();
        assertEquals(employeeId, actualTask.getEmployee());
        assertEquals(request.getName(), actualTask.getName());
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

    @Test
    void startTaskCountdown() {
    }

    @Test
    void stopTaskCountdown() {
    }

    @Test
    void showAllTasks() {
    }

    @Test
    void clearEmployeeTrackingData() {
    }
}