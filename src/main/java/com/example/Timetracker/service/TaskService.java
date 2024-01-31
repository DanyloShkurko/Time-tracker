package com.example.Timetracker.service;

import com.example.Timetracker.entity.Task;
import com.example.Timetracker.model.TaskResponse;

import java.util.List;

public interface TaskService {
    void startTaskCountdown(String taskId, String employeeId);
    void stopTaskCountdown(String taskId, String employeeId);
    List<TaskResponse> showAllTasks();
    void clearEmployeeTrackingData(String employeeId);
}
