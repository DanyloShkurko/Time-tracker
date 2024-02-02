package com.example.Timetracker.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequest {

    @NotBlank(message = "Employee id is mandatory!")
    private String employeeId;

    @Size(max = 50, message = "Task name should be less than 50 characters long!")
    @NotBlank(message = "Name is mandatory!")
    private String name;
}
