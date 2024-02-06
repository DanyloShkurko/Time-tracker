package com.example.Timetracker.controller;

import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.model.TaskResponse;
import com.example.Timetracker.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody @Valid EmployeeRequest employee) {
        return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.OK);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> editEmployeeInfo(@PathVariable String employeeId,
                                                             @RequestBody @Valid EmployeeRequest employeeRequest) {
        return new ResponseEntity<>(employeeService.editEmployeeInfo(employeeId, employeeRequest), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}/tasks")
    public ResponseEntity<List<TaskResponse>> showEmployeeEfforts(@PathVariable String employeeId,
                                                                  @RequestParam(name = "start") String start,
                                                                  @RequestParam(name = "end") String end) {
        Instant startInstant = convertStringToInstant(start);
        Instant endInstant = convertStringToInstant(end);

        return new ResponseEntity<>(employeeService.showEmployeeEfforts(employeeId, startInstant, endInstant), HttpStatus.OK);
    }

    @GetMapping("/{employeeId}/total-time")
    public ResponseEntity<String> showTheAmountOfLaborCostsForAllEmployeeTasks(@PathVariable String employeeId,
                                                                  @RequestParam(name = "start") String start,
                                                                  @RequestParam(name = "end") String end) {
        Instant startInstant = convertStringToInstant(start);
        Instant endInstant = convertStringToInstant(end);

        return new ResponseEntity<>(employeeService.showTheAmountOfLaborCostsForAllEmployeeTasks(employeeId, startInstant, endInstant), HttpStatus.OK);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> removeEmployee(@PathVariable String employeeId){
        employeeService.removeEmployee(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Instant convertStringToInstant(String date){
        try {
            return LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant();
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }
}
