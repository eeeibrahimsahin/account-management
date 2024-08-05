package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.exception.AuthenticationException;
import com.abc.bank.accountmanagement.exception.UsernameAlreadyExistsException;
import com.abc.bank.accountmanagement.mapper.CustomerMapper;
import com.abc.bank.accountmanagement.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

    @MockBean
    private DatabaseService databaseService;

    @MockBean
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerService customerService;

    private CustomerRegistrationRequestDTO validRequest;
    private Customer customer;

    @BeforeEach
    public void setup() {
        validRequest = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .idDocumentNumber("123456789")
                .username("alex")
                .build();

        customer = Customer.builder()
                .id(1l)
                .name("Alex Souza")
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .idDocument("123456789")
                .iban("NL12345567")
                .username("alex")
                .password("12345")
                .build();
    }

    @Test
    @DisplayName("Register should succeed when username is available")
    public void testRegisterSuccess() {
        //Arrange
        given(databaseService.checkUsernameAvailability(any())).willReturn(true);
        given(customerMapper.toCustomer(any(CustomerRegistrationRequestDTO.class))).willReturn(customer);
        given(databaseService.saveCustomer(any(Customer.class))).willReturn(customer);
        given(customerMapper.toCustomerRegistrationResponseDTO(any(Customer.class)))
                .willReturn(CustomerRegistrationResponseDTO.builder().username("alex").password("12345").iban("NL12345567").build());

        CustomerRegistrationResponseDTO response = customerService.register(validRequest);

        assertNotNull(response);
        assertEquals("alex", response.getUsername());
        verify(databaseService, times(1)).checkUsernameAvailability("alex");
        verify(databaseService, times(1)).saveCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("Register should fail when username is already taken")
    public void testRegisterUsernameAlreadyExists() {
        given(databaseService.checkUsernameAvailability(any())).willReturn(false);

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            customerService.register(validRequest);
        });

        verify(databaseService, times(1)).checkUsernameAvailability("alex");
        verify(databaseService, never()).saveCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("Login should succeed with correct credentials")
    public void testLoginSuccess() {
        given(databaseService.findCustomerByUsername(any())).willReturn(customer);

        Customer loggedInCustomer = customerService.login("alex", "12345");

        assertNotNull(loggedInCustomer);
        assertEquals("alex", loggedInCustomer.getUsername());
        verify(databaseService, times(1)).findCustomerByUsername("alex");
    }

    @Test
    @DisplayName("Login should fail with incorrect username")
    public void testLoginUsernameNotFound() {
        given(databaseService.findCustomerByUsername(anyString())).willReturn(null);

        assertThrows(AuthenticationException.class, () -> {
            customerService.login("wrongUsername", "12345");
        });

        verify(databaseService, times(1)).findCustomerByUsername("wrongUsername");
    }

    @Test
    @DisplayName("Login should fail with incorrect password")
    public void testLoginIncorrectPassword() {
        given(databaseService.findCustomerByUsername(anyString())).willReturn(customer);

        assertThrows(AuthenticationException.class, () -> {
            customerService.login("alex", "wrongPassword");
        });

        verify(databaseService, times(1)).findCustomerByUsername("alex");
    }
}