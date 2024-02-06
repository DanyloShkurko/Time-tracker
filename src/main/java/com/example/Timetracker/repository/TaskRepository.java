package com.example.Timetracker.repository;

import com.example.Timetracker.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {
    List<Task> findTasksByEmployeeAndEndTimeBetween(String employeeId, Instant start, Instant end);
    void deleteTasksByEmployee(String employeeId);
}