package com.example.Timetracker.controller;

import com.example.Timetracker.model.TaskRequest;
import com.example.Timetracker.model.TaskResponse;
import com.example.Timetracker.service.TaskService;
import com.example.Timetracker.utlil.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    TaskController taskController;

    @MockBean
    private TaskService taskService;

    private final String URL = "/api/v1/task";

    /* ========================================================================================= */
    /* =================================== CREATE TASK TESTS =================================== */
    /* ========================================================================================= */

    @Test
    void createTask_SuccessScenario() throws Exception {
        TaskResponse response = TaskResponse.builder()
                .id("1")
                .employee("2")
                .name("name")
                .build();

        String request = "{\"employeeId\": \"2\",\"name\": \"name\"}";

        given(taskService.createTask(any(TaskRequest.class))).willReturn(response);

        mockMvc.perform(post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("employee", Is.is("2")))
                .andExpect(jsonPath("name", Is.is("name")))
                .andExpect(jsonPath("start_time", Matchers.nullValue()))
                .andExpect(jsonPath("end_time", Matchers.nullValue()));
    }

    @Test
    void createTaskInvalidName_FailureScenario() throws Exception {
        String name = "n".repeat(51);

        String request = "{\"employeeId\": \"2\",\"name\": \"" + name + "\"}";

        mockMvc.perform(post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("name", Is.is("Task name should be less than 50 characters long!")));
    }

    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */

    /* ======================================================================================== */
    /* ============================== START TASK COUNTDOWN TESTS ============================== */
    /* ======================================================================================== */

    @Test
    void startTaskCountdown_FailureScenario() throws Exception {
        String id = "1";

        doThrow(EntityNotFoundException.class).when(taskService).startTaskCountdown(any(String.class));

        mockMvc.perform(post(URL + "/" + id + "/start"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void startTaskCountdown_SuccessScenario() throws Exception {
        String id = "1";

        doNothing().when(taskService).startTaskCountdown(any(String.class));

        mockMvc.perform(post(URL + "/" + id + "/start"))
                .andExpect(status().isOk());

        verify(taskService).startTaskCountdown(id);
    }

    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */

    /* ======================================================================================== */
    /* ==================================== END TASK TESTS ==================================== */
    /* ======================================================================================== */

    @Test
    void stopTaskCountdown_SuccessScenario() throws Exception {
        String id = "1";

        doNothing().when(taskService).stopTaskCountdown(any(String.class));

        mockMvc.perform(post(URL + "/" + id + "/stop"))
                .andExpect(status().isOk());

        verify(taskService).stopTaskCountdown(id);
    }

    @Test
    void stopTaskCountdown_FailureScenario() throws Exception {
        String id = "1";

        doThrow(EntityNotFoundException.class).when(taskService).stopTaskCountdown(any(String.class));

        mockMvc.perform(post(URL + "/" + id + "/stop"))
                .andExpect(status().isBadRequest());
    }

    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */

    /* ======================================================================================== */
    /* ===================== SHOW ALL WORK INTERVALS BY EMPLOYEE ID TESTS ===================== */
    /* ======================================================================================== */

    @Test
    void showAllWorkIntervalsByEmployeeId_SuccessScenario() throws Exception {
        String employee = UUID.randomUUID().toString();

        String start = "2024-02-01";
        String end = "2024-03-10";

        Instant startInstant = LocalDate.parse(start).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = LocalDate.parse(end).atStartOfDay(ZoneId.systemDefault()).toInstant();

        String request = "/" + employee + "?start=" + start + "&end=" + end;

        List<TaskResponse> taskResponses = List.of(
                new TaskResponse("1", employee, "test", Instant.parse("2024-02-01T10:00:00Z"), Instant.parse("2024-02-01T11:00:00Z")),
                new TaskResponse("2", employee, "test", Instant.parse("2024-02-02T10:00:00Z"), Instant.parse("2024-02-02T12:00:00Z")),
                new TaskResponse("3", employee, "test", Instant.parse("2024-02-03T10:00:00Z"), Instant.parse("2024-02-03T10:30:00Z"))
        );

        String expected = "[\n" +
                "    {\n" +
                "        \"id\": \"1\",\n" +
                "        \"employee\": \"" + employee + "\",\n" +
                "        \"name\": \"test\",\n" +
                "        \"start_time\": \"2024-02-01T10:00:00Z\",\n" +
                "        \"end_time\": \"2024-02-01T11:00:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"2\",\n" +
                "        \"employee\": \"" + employee + "\",\n" +
                "        \"name\": \"test\",\n" +
                "        \"start_time\": \"2024-02-02T10:00:00Z\",\n" +
                "        \"end_time\": \"2024-02-02T12:00:00Z\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"id\": \"3\",\n" +
                "        \"employee\": \"" + employee + "\",\n" +
                "        \"name\": \"test\",\n" +
                "        \"start_time\": \"2024-02-03T10:00:00Z\",\n" +
                "        \"end_time\": \"2024-02-03T10:30:00Z\"\n" +
                "    }\n" +
                "]\n";

        given(taskService.showAllWorkIntervalsByEmployeeId(eq(employee), eq(startInstant), eq(endInstant))).willReturn(taskResponses);

        mockMvc.perform(get(URL + request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));
    }

    @Test
    void showAllWorkIntervalsByEmployeeId_FailureScenario() throws Exception {
        String employee = "-1";

        String start = "2024-02-01";
        String end = "2024-03-10";

        Instant startInstant = LocalDate.parse(start).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = LocalDate.parse(end).atStartOfDay(ZoneId.systemDefault()).toInstant();

        String request = "/" + employee + "?start=" + start + "&end=" + end;

        given(taskService.showAllWorkIntervalsByEmployeeId(eq(employee), eq(startInstant), eq(endInstant))).willThrow((new EntityNotFoundException("Employee not found!")));

        mockMvc.perform(get(URL + request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee not found!"));
    }

    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */

    /* ======================================================================================== */
    /* ========================== CLEAR EMPLOYEE TRACKING DATA TESTS ========================== */
    /* ======================================================================================== */

    @Test
    void clearEmployeeTrackingData_SuccessScenario() throws Exception {
        String id = UUID.randomUUID().toString();
        String request = URL + "/"+id+"/task-entries";

        doNothing().when(taskService).clearEmployeeTrackingData(id);

        mockMvc.perform(delete(request))
                .andExpect(status().isOk());

        verify(taskService).clearEmployeeTrackingData(id);
    }

    @Test
    void clearEmployeeTrackingData_FailureScenario() throws Exception {
        String id = UUID.randomUUID().toString();
        String request = URL + "/"+id+"/task-entries";

        doThrow(new EntityNotFoundException("Employee not found!")).when(taskService).clearEmployeeTrackingData(id);

        mockMvc.perform(delete(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee not found!"))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof EntityNotFoundException));
    }

    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */
}