package com.abc.bank.accountmanagement.mapper;

import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerMapperTest {
    @Autowired
    private CustomerMapper customerMapper;

    @Test
    @DisplayName("Convert CustomerRegistrationRequestDTO to Customer")
    public void testToCustomer() {
        CustomerRegistrationRequestDTO requestDTO = CustomerRegistrationRequestDTO.builder()
                .name("Alex Souza")
                .address("123 Main St")
                .dateOfBirth("1990-01-01")
                .idDocumentNumber("123456789")
                .username("alex")
                .build();

        Customer customer = customerMapper.toCustomer(requestDTO);

        assertNotNull(customer);
        assertEquals("Alex Souza", customer.getName());
        assertEquals("alex", customer.getUsername());
        assertEquals("123 Main St", customer.getAddress());
        assertEquals("1990-01-01", customer.getDateOfBirth());
        assertEquals("123456789", customer.getIdDocument());
    }

    @Test
    @DisplayName("Convert Customer to CustomerRegistrationResponseDTO")
    public void testToCustomerRegistrationResponseDTO() {
        Customer customer = Customer.builder()
                .username("alex")
                .password("12345")
                .iban("NL12345567")
                .build();

        CustomerRegistrationResponseDTO responseDTO = customerMapper.toCustomerRegistrationResponseDTO(customer);

        assertNotNull(responseDTO);
        assertEquals("alex", responseDTO.getUsername());
        assertEquals("12345", responseDTO.getPassword());
        assertEquals("NL12345567", responseDTO.getIban());
    }
}