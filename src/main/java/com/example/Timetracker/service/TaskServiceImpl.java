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

import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class TaskServiceImpl implements TaskService{
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
    public void startTaskCountdown(String taskId, String employeeId) {

    }

    @Override
    public void stopTaskCountdown(String taskId, String employeeId) {

    }

    @Override
    public List<TaskResponse> showAllTasks() {
        return null;
    }

    @Override
    public void clearEmployeeTrackingData(String employeeId) {

    }
}
