package com.example.Timetracker.service;

import com.example.Timetracker.entity.Task;
import com.example.Timetracker.model.TaskRequest;
import com.example.Timetracker.model.TaskResponse;
import com.example.Timetracker.repository.EmployeeRepository;
import com.example.Timetracker.repository.TaskRepository;
import com.example.Timetracker.utlil.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        log.info("Saving new task...");

        employeeRepository.findById(taskRequest.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee doesn't exist!"));

        Task newTask = Task.builder()
                .id(UUID.randomUUID().toString())
                .employee(taskRequest.getEmployeeId())
                .name(taskRequest.getName())
                .build();

        taskRepository.save(newTask);

        log.info("Task saved successfully with id: {}", newTask.getId());

        return TaskResponse.builder()
                .id(newTask.getId())
                .employee(newTask.getEmployee())
                .name(newTask.getName())
                .build();
    }

    @Override
    public void startTaskCountdown(String taskId) {
        log.info("Is timed for the task with id: {}", taskId);

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("There is no task with id: " + taskId));
        task.setStartTime(Instant.now());
        taskRepository.save(task);

        log.info("The start time of the task is saved!");
    }

    @Override
    public void stopTaskCountdown(String taskId) {
        log.info("Saving the task end time...");

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("There is no task with id: " + taskId));
        task.setEndTime(Instant.now());
        taskRepository.save(task);

        log.info("The end time of the task is saved!");

    }

    @Override
    public List<TaskResponse> showAllWorkIntervalsByEmployeeId(String id, Instant start, Instant end) {
        log.info("Looking for an employee based on his id...");
        employeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee not found!"));

        log.info("Looking for all time slots occupied by work...");
        List<Task> tasks = taskRepository.findTasksByEmployeeAndEndTimeBetween(id, start, end);

        log.info("There they are!");
        log.info(tasks);

        return tasks.stream()
                .map(e -> TaskResponse.builder()
                        .id(e.getId())
                        .name(e.getName())
                        .employee(e.getEmployee())
                        .start_time(e.getStartTime())
                        .end_time(e.getEndTime())
                        .build())
                .toList();
    }

    @Override
    public void clearEmployeeTrackingData(String employeeId) {

    }
}
