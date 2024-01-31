package com.example.Timetracker.model;

import com.example.Timetracker.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {
    private String id;
    private Employee employee;
    private String name;
    private LocalDate start_time;
    private LocalDate end_time;
}
