package com.example.Timetracker.controller;

import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.service.EmployeeService;
import com.example.Timetracker.utlil.EntityNotFoundException;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EmployeeController employeeController;

    @MockBean
    private EmployeeService employeeService;

    private final String URL = "/api/v1/employee";


    /* ========================================================================================= */
    /* ================================= CREATE EMPLOYEE TESTS ================================= */
    /* ========================================================================================= */
    @Test
    void createEmployee_SuccessScenario() throws Exception {
        String employee = "{\"employee_name\": \"bob\", \"email\" : \"bob@gmail.com\"}";
        mockMvc.perform(post(URL)
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createEmployeeWithNullParameters_FailureScenario() throws Exception {
        String request = "{\"employee_name\": null, \"email\": null}";

        mockMvc.perform(post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("employee_name", Is.is("Name is mandatory")))
                .andExpect(jsonPath("email", Is.is("Email is mandatory")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createEmployeeWithWrongLengthParameters_FailureScenario() throws Exception {
        String employee = "{\"employee_name\": \"" + "a".repeat(51) + "\", \"email\": \"" + "a".repeat(66) + "@gmail.com\" }";

        mockMvc.perform(post(URL)
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("employee_name", Is.is("The name must be less than 50 characters long!")))
                .andExpect(jsonPath("email", Is.is("The email must be less than 75 characters long!")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createEmployeeWithWrongEmailParameters_FailureScenario() throws Exception {
        String employee = "{\"employee_name\": \"bob\", \"email\" : \"bob@.cm\"}";

        mockMvc.perform(post(URL)
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("email", Is.is("Email should be valid")))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */


    /* ========================================================================================= */
    /* ================================== EDIT EMPLOYEE TESTS ================================== */
    /* ========================================================================================= */

    @Test
    void editEmployeeInfoTest_SuccessScenario() throws Exception {
        String employeeId = "1";
        String employee = "{\"employee_name\": \"test\", \"email\": \"test@gmail.com\"}";
        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(employeeId)
                .employee_name("test")
                .email("test@gmail.com")
                .build();

        given(employeeService.editEmployeeInfo(eq(employeeId), any(EmployeeRequest.class))).willReturn(employeeResponse);

        mockMvc.perform(put(URL + "/" + employeeId)
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Is.is(employeeResponse.getId())))
                .andExpect(jsonPath("employee_name", Is.is(employeeResponse.getEmployee_name())))
                .andExpect(jsonPath("email", Is.is(employeeResponse.getEmail())));
    }

    @Test
    void editEmployeeInfoTest_FailureScenario() throws Exception {
        String employeeId = "-1";
        String employee = "{\"employee_name\": \"test\", \"email\": \"test@gmail.com\"}";

        willThrow(new EntityNotFoundException("Employee not found!"))
                .given(employeeService).editEmployeeInfo(eq(employeeId), any(EmployeeRequest.class));

        mockMvc.perform(put(URL + "/" + employeeId)
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee not found!"));
    }

    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */

    /* ========================================================================================= */
    /* ============================== SHOW EMPLOYEE EFFORTS TESTS ============================== */
    /* ========================================================================================= */

    @Test
    void showEmployeeEffortsTest_SuccessScenario() throws Exception {
        String employeeId = "1";

        String start = "2024-02-01";
        String end = "2024-02-01";

        Instant startInstant = LocalDate.parse(start).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = LocalDate.parse(end).atStartOfDay(ZoneId.systemDefault()).toInstant();

        String request = "/" + employeeId + "/tasks?start=" + start + "&end=" + end;

        given(employeeService.showEmployeeEfforts(eq(employeeId), eq(startInstant), eq(endInstant))).willReturn(List.of());

        mockMvc.perform(get(URL + request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void showEmployeeEffortsTest_FailureScenario_WrongFormatData() throws Exception {
        String employeeId = "1";

        String start = "2024-02-02T11:18:04.476145Z";
        String end = "2024-03-03T11:18:04.476145Z";

        String request = "/" + employeeId + "/tasks?start=" + start + "1&end=" + end;

        mockMvc.perform(get(URL + request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dates entered must be in the format yyyy-MM-dd"));
    }

    @Test
    void showEmployeeEffortsTest_FailureScenario_WrongEmployeeId() throws Exception {
        String employeeId = UUID.randomUUID().toString();

        String start = "2024-02-01";
        String end = "2024-02-01";


        Instant startInstant = LocalDate.parse(start).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = LocalDate.parse(end).atStartOfDay(ZoneId.systemDefault()).toInstant();

        String request = "/" + employeeId + "/tasks?start=" + start + "&end=" + end;

        given(employeeService.showEmployeeEfforts(anyString(), eq(startInstant), eq(endInstant))).willThrow(new EntityNotFoundException("Employee not found!"));

        mockMvc.perform(get(URL + request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee not found!"));
    }

    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */

    /* ======================================================================================== */
    /* ========================== SHOW SUM OF EMPLOYEE EFFORTS TESTS ========================== */
    /* ======================================================================================== */

    @Test
    void showSumOfEmployeeEffortsTest_SuccessScenario() throws Exception {
        String employeeId = "1";

        String start = "2024-02-01";
        String end = "2024-02-01";

        Instant startInstant = LocalDate.parse(start).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = LocalDate.parse(end).atStartOfDay(ZoneId.systemDefault()).toInstant();

        String request = "/" + employeeId + "/total-time?start=" + start + "&end=" + end;

        String expected = "3:21";

        given(employeeService.showTheAmountOfLaborCostsForAllEmployeeTasks(eq(employeeId), eq(startInstant), eq(endInstant))).willReturn(expected);

        mockMvc.perform(get(URL + request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void showSumOfEmployeeEffortsTest_FailureScenario_WrongDataFormat() throws Exception {
        String employeeId = "1";

        String start = "2024-02-02T11:18:04.476145Z";
        String end = "2024-03-03T11:18:04.476145Z";

        String request = "/" + employeeId + "/total-time?start=" + start + "&end=" + end;

        mockMvc.perform(get(URL + request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Dates entered must be in the format yyyy-MM-dd"));
    }
    @Test
    void showSumOfEmployeeEffortsTest_FailureScenario_WrongEmployeeId() throws Exception {
        String employeeId = UUID.randomUUID().toString();

        String start = "2024-02-01";
        String end = "2024-02-01";


        Instant startInstant = LocalDate.parse(start).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = LocalDate.parse(end).atStartOfDay(ZoneId.systemDefault()).toInstant();

        String request = "/" + employeeId + "/total-time?start=" + start + "&end=" + end;

        given(employeeService.showTheAmountOfLaborCostsForAllEmployeeTasks(anyString(), eq(startInstant), eq(endInstant))).willThrow(new EntityNotFoundException("Employee not found!"));

        mockMvc.perform(get(URL + request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee not found!"));
    }


    /* ======================================================================================== */
    /* ======================================================================================== */
    /* ======================================================================================== */

}