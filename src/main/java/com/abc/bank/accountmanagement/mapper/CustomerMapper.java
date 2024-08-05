package com.abc.bank.accountmanagement.mapper;

import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {


    public Customer toCustomer(CustomerRegistrationRequestDTO customerRegistrationRequestDTO) {
        return Customer.builder()
                .name(customerRegistrationRequestDTO.getName())
                .username(customerRegistrationRequestDTO.getUsername())
                .address(customerRegistrationRequestDTO.getAddress())
                .dateOfBirth(customerRegistrationRequestDTO.getDateOfBirth())
                .idDocument(customerRegistrationRequestDTO.getIdDocumentNumber())
                .build();
    }

    public CustomerRegistrationResponseDTO toCustomerRegistrationResponseDTO(Customer customer) {
        return CustomerRegistrationResponseDTO.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
                .iban(customer.getIban())
                .build();
    }
}
