package com.example.Timetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "Task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private String id;

    @Column(name = "employee")
    private String employee;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "startTime")
    private Instant startTime;

    @Column(name = "endTime")
    private Instant endTime;
}
