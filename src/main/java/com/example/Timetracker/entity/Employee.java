package com.example.Timetracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "employee")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "employee_name", length = 50)
    private String employee_name;

    @Column(name = "email", length = 75)
    private String email;
}