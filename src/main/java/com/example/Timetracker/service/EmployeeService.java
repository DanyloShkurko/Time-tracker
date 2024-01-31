package com.example.Timetracker.service;

import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.model.TaskResponse;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    String createEmployee(EmployeeRequest employee);
    EmployeeResponse editEmployeeInfo(String employeeId, EmployeeRequest employee);
    List<TaskResponse> showEmployeeEfforts(String employeeId, LocalDate n, LocalDate m);
    double showTheAmountOfLaborCostsForAllEmployeeTasks(String employeeId);
}