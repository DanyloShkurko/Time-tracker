package com.example.Timetracker.service;

import com.example.Timetracker.entity.Employee;
import com.example.Timetracker.entity.Task;
import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.model.TaskResponse;
import com.example.Timetracker.repository.EmployeeRepository;
import com.example.Timetracker.repository.TaskRepository;
import com.example.Timetracker.utlil.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public String createEmployee(EmployeeRequest employeeRequest) {
        log.info("Employee creating...");
        Employee employee = Employee.builder()
                .id(UUID.randomUUID().toString())
                .employee_name(employeeRequest.getEmployee_name())
                .email(employeeRequest.getEmail())
                .build();

        employeeRepository.save(employee);

        log.info("Employee created with id: {}", employee.getId());
        return employee.getId();
    }

    @Override
    public EmployeeResponse editEmployeeInfo(String employeeId, EmployeeRequest employeeRequest) {
        log.info("Looking for an employee based on his id...");
        employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found!"));

        log.info("Saving employee changes...");

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found!"));

        employee.setEmployee_name(employeeRequest.getEmployee_name());
        employee.setEmail(employeeRequest.getEmail());

        Employee updatedEmployee = employeeRepository.save(employee);

        log.info("Changes are saved for the employee by id: {}", employeeId);

        return EmployeeResponse.builder()
                .id(updatedEmployee.getId())
                .employee_name(updatedEmployee.getEmployee_name())
                .email(updatedEmployee.getEmail())
                .build();
    }


    @Override
    public List<TaskResponse> showEmployeeEfforts(String employeeId, Instant start, Instant end) {
        log.info("Retrieving employee efforts for employeeId: {}, from: {}, to: {}", employeeId, start, end);
        log.info("Looking for an employee based on his id...");
        employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found!"));

        List<Task> employeeEfforts = taskRepository.findTasksByEmployeeAndEndTimeBetween(employeeId, start, end);
        List<TaskResponse> responses = employeeEfforts.stream()
                .map(e -> TaskResponse.builder()
                        .id(e.getId())
                        .employee(e.getEmployee())
                        .name(e.getName())
                        .start_time(e.getStartTime())
                        .end_time(e.getEndTime())
                        .build())
                .sorted((task1, task2) -> {
                    Duration duration1 = Duration.between(task1.getStart_time(), task1.getEnd_time());
                    Duration duration2 = Duration.between(task2.getStart_time(), task2.getEnd_time());
                    return duration2.compareTo(duration1);
                })
                .toList();

        log.info("Found {} efforts for employeeId: {}", responses.size(), employeeId);

        return responses;
    }


    @Override
    public String showTheAmountOfLaborCostsForAllEmployeeTasks(String employeeId, Instant start, Instant end) {
        log.info("Calculating the amount of labor costs for all tasks of employeeId: {}, from: {}, to: {}", employeeId, start, end);
        log.info("Looking for an employee based on his id...");
        employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found!"));

        List<Task> employeeEfforts = taskRepository.findTasksByEmployeeAndEndTimeBetween(employeeId, start, end);

        Duration totalDuration = Duration.ZERO;
        for (Task task : employeeEfforts) {
            Duration duration = Duration.between(task.getStartTime(), task.getEndTime());
            totalDuration = totalDuration.plus(duration);
        }

        long hours = totalDuration.toHours();
        long minutes = totalDuration.toMinutes() % 60;

        String result = String.format("%02d:%02d", hours, minutes);
        log.info("Total labor costs for employeeId: {} is {}", employeeId, result);
        return result;
    }


    @Override
    @Transactional
    public void removeEmployee(String employeeId) {
        log.info("Looking for an employee based on his id...");
        employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found!"));

        log.info("Removing all employee's tasks...");
        taskRepository.deleteTasksByEmployee(employeeId);

        log.info("Removing employee...");
        employeeRepository.deleteById(employeeId);
    }
}