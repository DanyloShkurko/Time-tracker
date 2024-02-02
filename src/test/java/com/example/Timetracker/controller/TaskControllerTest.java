package com.example.Timetracker.controller;

import com.example.Timetracker.model.TaskRequest;
import com.example.Timetracker.model.TaskResponse;
import com.example.Timetracker.service.TaskService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}