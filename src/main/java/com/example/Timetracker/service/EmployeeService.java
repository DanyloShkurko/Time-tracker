package com.example.Timetracker.service;

import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.model.TaskResponse;

import java.time.Instant;
import java.util.List;

public interface EmployeeService {
    String createEmployee(EmployeeRequest employee);
    EmployeeResponse editEmployeeInfo(String employeeId, EmployeeRequest employeeRequest);
    List<TaskResponse> showEmployeeEfforts(String employeeId, Instant start, Instant end);
    String showTheAmountOfLaborCostsForAllEmployeeTasks(String employeeId, Instant start, Instant end);
}