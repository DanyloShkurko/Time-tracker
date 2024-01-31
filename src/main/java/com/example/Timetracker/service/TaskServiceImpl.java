package com.example.Timetracker.service;

import com.example.Timetracker.entity.Task;
import com.example.Timetracker.repository.TaskRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class TaskServiceImpl implements TaskService{
    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void startTaskCountdown(String taskId, String employeeId) {

    }

    @Override
    public void stopTaskCountdown(String taskId, String employeeId) {

    }

    @Override
    public List<Task> showAllTasks() {
        return null;
    }

    @Override
    public void clearEmployeeTrackingData(String employeeId) {

    }
}
