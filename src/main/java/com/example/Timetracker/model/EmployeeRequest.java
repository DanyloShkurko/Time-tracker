package com.example.Timetracker.model;

import jakarta.validation.constraints.Email;
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
public class EmployeeRequest {

    @Size(max = 50, message = "The name must be less than 50 characters long!")
    @NotBlank(message = "Name is mandatory")
    private String employee_name;

    @Size(max = 75, message = "The email must be less than 75 characters long!")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;
}
