package com.abc.bank.accountmanagement.exception;

import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.repository.CustomerRepository;
import com.abc.bank.accountmanagement.service.CustomerService;
import com.abc.bank.accountmanagement.service.DatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    private String validJson;
    private String invalidJson;
    private String usernameAlreadyExistsJson;

    @BeforeEach
    public void setup() {
        validJson = "{\n" +
                "  \"name\": \"Alex Souza\",\n" +
                "  \"address\": \"123 Main St\",\n" +
                "  \"dateOfBirth\": \"1990-01-01\",\n" +
                "  \"idDocumentNumber\": \"123456789\",\n" +
                "  \"username\": \"alex\"\n" +
                "}";

        invalidJson = "{\n" +
                "  \"name\": \"Alex\",\n" +
                "  \"address\": \"\",\n" +
                "  \"dateOfBirth\": \"1990-01-01\",\n" +
                "  \"idDocumentNumber\": \"123456789\",\n" +
                "  \"username\": \"alex\"\n" +
                "}";

        usernameAlreadyExistsJson = "{\n" +
                "  \"name\": \"Alex Souza\",\n" +
                "  \"address\": \"123 Main St\",\n" +
                "  \"dateOfBirth\": \"1990-01-01\",\n" +
                "  \"idDocumentNumber\": \"123456789\",\n" +
                "  \"username\": \"existinguser\"\n" +
                "}";
    }

    @Test
    @DisplayName("Handle MethodArgumentNotValidException")
    public void testHandleMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Validation failed"))
                .andExpect(jsonPath("errors[0].field").value("address"))
                .andExpect(jsonPath("errors[0].message").value("Address is mandatory"));
    }

    @Test
    @DisplayName("Handle UsernameAlreadyExistsException")
    public void testHandleUsernameAlreadyExistsException() throws Exception {
        given(customerService.register(any(CustomerRegistrationRequestDTO.class)))
                .willThrow(new UsernameAlreadyExistsException("Username already exists"));

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usernameAlreadyExistsJson))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("Username already exists"));
    }

    @Test
    @DisplayName("Handle TooManyRequestsException")
    public void testHandleTooManyRequestsException() throws Exception {
        given(customerService.register(any()))
                .willThrow(new TooManyRequestsException("Too many requests"));

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andDo(print())
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("message").value("Too many requests"));
    }

    @Test
    @DisplayName("Handle AuthenticationException")
    public void testHandleAuthenticationException() throws Exception {
        given(customerService.register(any()))
                .willThrow(new AuthenticationException("Unauthorized") {
                });

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value("Unauthorized"));
    }

//    @Test
//    @DisplayName("Handle UsernameNotFoundException")
//    public void testHandleUsernameNotFoundException() throws Exception {
//        given(customerService.register(any()))
//                .willThrow(new UsernameNotFoundException("Username not found"));
//
//        mockMvc.perform(post("/api/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(validJson))
//                .andDo(print())
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("message").value("Username not found"));
//    }
}