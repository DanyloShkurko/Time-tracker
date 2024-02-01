package com.example.Timetracker.controller;

import com.example.Timetracker.model.EmployeeRequest;
import com.example.Timetracker.model.EmployeeResponse;
import com.example.Timetracker.service.EmployeeService;
import com.example.Timetracker.utlil.EntityNotFoundException;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EmployeeController employeeController;

    @MockBean
    private EmployeeService employeeService;


    /* ========================================================================================= */
    /* ================================= CREATE EMPLOYEE TESTS ================================= */
    /* ========================================================================================= */
    @Test
    void createEmployee_SuccessScenario() throws Exception {
        String employee = "{\"employee_name\": \"bob\", \"email\" : \"bob@gmail.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employee")
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void createEmployeeWithNullParameters_FailureScenario() throws Exception {
        String request = "{\"employee_name\": null, \"email\": null}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employee")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("employee_name", Is.is("Name is mandatory")))
                .andExpect(MockMvcResultMatchers.jsonPath("email", Is.is("Email is mandatory")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createEmployeeWithWrongLengthParameters_FailureScenario() throws Exception {
        String employee = "{\"employee_name\": \"" + "a".repeat(51) + "\", \"email\": \"" + "a".repeat(66) + "@gmail.com\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employee")
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("employee_name", Is.is("The name must be less than 50 characters long!")))
                .andExpect(MockMvcResultMatchers.jsonPath("email", Is.is("The email must be less than 75 characters long!")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createEmployeeWithWrongEmailParameters_FailureScenario() throws Exception {
        String employee = "{\"employee_name\": \"bob\", \"email\" : \"bob@.cm\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employee")
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("email", Is.is("Email should be valid")))
                .andExpect(MockMvcResultMatchers.content()
                        .contentType(MediaType.APPLICATION_JSON));
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

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employee/" + employeeId)
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

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employee/" + employeeId)
                        .content(employee)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Employee not found!"));
    }

    /* ========================================================================================= */
    /* ========================================================================================= */
    /* ========================================================================================= */
}