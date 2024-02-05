package com.example.Timetracker.service;

import com.example.Timetracker.model.TaskRequest;
import com.example.Timetracker.model.TaskResponse;

import java.time.Instant;
import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest);
    void startTaskCountdown(String taskId);
    void stopTaskCountdown(String taskId);
    List<TaskResponse> showAllWorkIntervalsByEmployeeId(String id, Instant start, Instant end);
    void clearEmployeeTrackingData(String employeeId);
}
