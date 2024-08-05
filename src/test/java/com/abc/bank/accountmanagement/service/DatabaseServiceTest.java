package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.exception.TooManyRequestsException;
import com.abc.bank.accountmanagement.exception.UsernameNotFoundException;
import com.abc.bank.accountmanagement.model.Customer;
import com.abc.bank.accountmanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class DatabaseServiceTest {

    @MockBean
    private RateLimiterService rateLimiterService;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private DatabaseService databaseService;

    private Customer customer;

    @BeforeEach
    public void setup() {
        customer =  customer = Customer.builder()
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
    @DisplayName("Save customer successfully")
    public void testSaveCustomerSuccess() {
        given(rateLimiterService.tryConsume()).willReturn(true);
        given(customerRepository.save(any(Customer.class))).willReturn(customer);

        Customer savedCustomer = databaseService.saveCustomer(customer);

        assertNotNull(savedCustomer);
        assertEquals("alex", savedCustomer.getUsername());
        verify(rateLimiterService, times(1)).tryConsume();
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @DisplayName("Save customer fails due to rate limit")
    public void testSaveCustomerRateLimitExceeded() {
        given(rateLimiterService.tryConsume()).willReturn(false);

        assertThrows(TooManyRequestsException.class, () -> {
            databaseService.saveCustomer(customer);
        });

        verify(rateLimiterService, times(1)).tryConsume();
        verify(customerRepository, never()).save(customer);
    }

    @Test
    @DisplayName("Find customer by username successfully")
    public void testFindCustomerByUsernameSuccess() {
        given(rateLimiterService.tryConsume()).willReturn(true);
        given(customerRepository.findByUsername(anyString())).willReturn(Optional.of(customer));

        Customer foundCustomer = databaseService.findCustomerByUsername("alex");

        assertNotNull(foundCustomer);
        assertEquals("alex", foundCustomer.getUsername());
        verify(rateLimiterService, times(1)).tryConsume();
        verify(customerRepository, times(1)).findByUsername("alex");
    }

    @Test
    @DisplayName("Find customer by username fails due to rate limit")
    public void testFindCustomerByUsernameRateLimitExceeded() {
        given(rateLimiterService.tryConsume()).willReturn(false);

        assertThrows(TooManyRequestsException.class, () -> {
            databaseService.findCustomerByUsername("alex");
        });

        verify(rateLimiterService, times(1)).tryConsume();
        verify(customerRepository, never()).findByUsername("alex");
    }

    @Test
    @DisplayName("Find customer by username throws UsernameNotFoundException")
    public void testFindCustomerByUsernameNotFound() {
        given(rateLimiterService.tryConsume()).willReturn(true);
        given(customerRepository.findByUsername(anyString())).willReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            databaseService.findCustomerByUsername("alex");
        });

        verify(rateLimiterService, times(1)).tryConsume();
        verify(customerRepository, times(1)).findByUsername("alex");
    }

    @Test
    @DisplayName("Check username availability successfully")
    public void testCheckUsernameAvailabilitySuccess() {
        given(rateLimiterService.tryConsume()).willReturn(true);
        given(customerRepository.findByUsername(anyString())).willReturn(Optional.empty());

        boolean isAvailable = databaseService.checkUsernameAvailability("alex");

        assertTrue(isAvailable);
        verify(rateLimiterService, times(1)).tryConsume();
        verify(customerRepository, times(1)).findByUsername("alex");
    }

    @Test
    @DisplayName("Check username availability fails due to rate limit")
    public void testCheckUsernameAvailabilityRateLimitExceeded() {
        given(rateLimiterService.tryConsume()).willReturn(false);

        assertThrows(TooManyRequestsException.class, () -> {
            databaseService.checkUsernameAvailability("alex");
        });

        verify(rateLimiterService, times(1)).tryConsume();
        verify(customerRepository, never()).findByUsername("alex");
    }
}