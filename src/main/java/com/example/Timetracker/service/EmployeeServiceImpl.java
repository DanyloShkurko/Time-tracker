package com.example.Timetracker.service;

import com.example.Timetracker.entity.Employee;
import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.model.TaskResponse;
import com.example.Timetracker.repository.EmployeeRepository;
import com.example.Timetracker.utlil.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
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
    public List<TaskResponse> showEmployeeEfforts(String employeeId, LocalDate n, LocalDate m) {
        return null;
    }

    @Override
    public double showTheAmountOfLaborCostsForAllEmployeeTasks(String employeeId) {
        return 0;
    }
}