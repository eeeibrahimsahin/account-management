package com.abc.bank.accountmanagement.controller;

import com.abc.bank.accountmanagement.dto.CustomerOverviewResponseDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.dto.LoginRequestDTO;
import com.abc.bank.accountmanagement.exception.AuthenticationException;
import com.abc.bank.accountmanagement.exception.UsernameAlreadyExistsException;
import com.abc.bank.accountmanagement.model.Customer;
import com.abc.bank.accountmanagement.repository.CustomerRepository;
import com.abc.bank.accountmanagement.service.CustomerService;
import com.abc.bank.accountmanagement.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;
import java.util.List;

import static com.abc.bank.accountmanagement.util.JsonUtil.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @MockBean
    CustomerRepository customerRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("Customer Registration Happy Flow Test")
    public void testCustomerRegistrationHappyFlow() throws Exception {
        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .idDocumentNumber("123456789")
                .username("alex")
                .build();
        given(customerService.register(any(CustomerRegistrationRequestDTO.class)))
                .willReturn(CustomerRegistrationResponseDTO.builder()
                        .username("alex")
                        .password("12345")
                        .iban("NL12345567")
                        .build());


        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("username").value("alex"))
                .andExpect(jsonPath("iban").value("NL12345567"))
                .andExpect(jsonPath("password").exists());

        verify(customerService).register(requestDTO);
    }

    @Test
    @DisplayName("Customer Registration Unhappy Flow Test - Username Already Exists")
    public void testCustomerRegistrationUnhappyFlow() throws Exception {
        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .idDocumentNumber("123456789")
                .username("alex")
                .build();

        doThrow(new UsernameAlreadyExistsException("Username already exists"))
                .when(customerService)
                .register(any(CustomerRegistrationRequestDTO.class));


        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username already exists"));

        verify(customerService).register(requestDTO);
    }

    @Test
    @DisplayName("Customer Registration - Name is Mandatory")
    public void testCustomerRegistrationNameIsMandatory() throws Exception {

        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .idDocumentNumber("123456789")
                .username("alex")
                .build();


        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].message").value("Name is mandatory"));
    }

    @Test
    @DisplayName("Customer Registration - Address is Mandatory")
    public void testCustomerRegistrationAddressIsMandatory() throws Exception {

        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .dateOfBirth("1990-01-01")
                .idDocumentNumber("123456789")
                .username("alex")
                .build();


        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].message").value("Address is mandatory"));
    }

    @Test
    @DisplayName("Customer Registration - Date of Birth is Mandatory")
    public void testCustomerRegistrationDateOfBirthIsMandatory() throws Exception {

        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .address("123 Main St")
                .idDocumentNumber("123456789")
                .username("alex")
                .build();


        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Customer Registration - ID Document Number is Mandatory")
    public void testCustomerRegistrationIdDocumentNumberIsMandatory() throws Exception {

        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .username("alex")
                .build();


        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].message").value("ID document number is mandatory"));
    }

    @Test
    @DisplayName("Customer Registration - Username is Mandatory")
    public void testCustomerRegistrationUsernameIsMandatory() throws Exception {

        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .idDocumentNumber("123456789")
                .build();


        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].message").value("Username is mandatory"));
    }

    @Test
    @DisplayName("Customer Registration - Username Length Validation")
    public void testCustomerRegistrationUsernameLength() throws Exception {

        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .idDocumentNumber("123456789")
                .username("thisusernameiswaytoolongforourvalidation")
                .build();


        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Successful login with correct username and password")
    public void testLoginSuccess() throws Exception {
        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .username("test")
                .password("test")
                .build();


        given(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test", "test")))
                .willReturn(null);


        mockMvc.perform(post("/api/logon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequestDTO)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Failed login with incorrect username or password")
    public void testLoginFailure() throws Exception {

        LoginRequestDTO loginRequestDTO = LoginRequestDTO.builder()
                .username("test")
                .password("wrongpassword")
                .build();

        doThrow(new AuthenticationException("Invalid username or password") {
        })
                .when(authenticationManager)
                .authenticate(new UsernamePasswordAuthenticationToken("test", "wrongpassword"));


        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequestDTO)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}

