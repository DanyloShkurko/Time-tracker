package com.example.Timetracker.service;

import com.example.Timetracker.entity.Employee;
import com.example.Timetracker.entity.Task;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);
    Employee editEmployeeInfo(Employee employee);
    List<Task> showEmployeeEfforts(String employeeId, LocalDate n, LocalDate m);
    double showTheAmountOfLaborCostsForAllEmployeeTasks(String employeeId);
}