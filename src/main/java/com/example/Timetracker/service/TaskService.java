package com.example.Timetracker.service;

import com.example.Timetracker.entity.Task;

import java.util.List;

public interface TaskService {
    void startTaskCountdown(String taskId, String employeeId);
    void stopTaskCountdown(String taskId, String employeeId);
    List<Task> showAllTasks();
    void clearEmployeeTrackingData(String employeeId);
}
