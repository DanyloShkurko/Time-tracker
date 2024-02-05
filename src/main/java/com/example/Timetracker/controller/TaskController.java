package com.example.Timetracker.controller;

import com.example.Timetracker.model.TaskRequest;
import com.example.Timetracker.model.TaskResponse;
import com.example.Timetracker.service.TaskService;
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
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody @Valid TaskRequest taskRequest){
        return new ResponseEntity<>(taskService.createTask(taskRequest), HttpStatus.OK);
    }

    @PostMapping("/{taskId}/start")
    public ResponseEntity<HttpStatus> startTaskCountdown(@PathVariable String taskId){
        taskService.startTaskCountdown(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{taskId}/stop")
    public ResponseEntity<HttpStatus> stopTaskCountdown(@PathVariable String taskId){
        taskService.stopTaskCountdown(taskId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<List<TaskResponse>> showAllWorkIntervalsByEmployeeId(@PathVariable String employeeId,
                                                                  @RequestParam(name = "start") String start,
                                                                  @RequestParam(name = "end") String end) {
        Instant startInstant = convertStringToInstant(start);
        Instant endInstant = convertStringToInstant(end);

        return new ResponseEntity<>(taskService.showAllWorkIntervalsByEmployeeId(employeeId, startInstant, endInstant), HttpStatus.OK);
    }

    public Instant convertStringToInstant(String date){
        try {
            return LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault()).toInstant();
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }
}
