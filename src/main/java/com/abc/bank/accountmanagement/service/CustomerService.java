package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.exception.AuthenticationException;
import com.abc.bank.accountmanagement.exception.UsernameAlreadyExistsException;
import com.abc.bank.accountmanagement.mapper.CustomerMapper;
import com.abc.bank.accountmanagement.model.Customer;
import com.abc.bank.accountmanagement.util.IbanUtil;
import com.abc.bank.accountmanagement.util.PasswordUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerService {
    private final DatabaseService databaseService;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerRegistrationResponseDTO register(CustomerRegistrationRequestDTO customerRegistrationRequestDTO) {
        if (!databaseService.checkUsernameAvailability(customerRegistrationRequestDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        Customer customer = customerMapper.toCustomer(customerRegistrationRequestDTO);
        String rawPassword = PasswordUtil.generateDefaultPassword();
        customer.setPassword(passwordEncoder.encode(rawPassword));
        customer.setIban(IbanUtil.generateIban());
        Customer savedCustomer = databaseService.saveCustomer(customer);
        savedCustomer.setPassword(rawPassword);

        return customerMapper.toCustomerRegistrationResponseDTO(savedCustomer);

    }


    public Customer login(String username, String password) {
        Customer customer = databaseService.findCustomerByUsername(username);
        if (customer == null || !password.equals(customer.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }
        return customer;
    }

}
