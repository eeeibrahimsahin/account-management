package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.dto.CustomerOverviewResponseDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.exception.UsernameAlreadyExistsException;
import com.abc.bank.accountmanagement.mapper.CustomerMapper;
import com.abc.bank.accountmanagement.model.Customer;
import com.abc.bank.accountmanagement.util.IbanUtil;
import com.abc.bank.accountmanagement.util.PasswordUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final DatabaseService databaseService;
    private final PasswordEncoder passwordEncoder;

    public List<CustomerOverviewResponseDTO> getAll() {
        return CustomerMapper.INSTANCE.customersToCustomerOverviewResponseDTOs(databaseService.getAll());
    }

    public CustomerRegistrationResponseDTO register(CustomerRegistrationRequestDTO customerRegistrationRequestDTO) {
        if (!databaseService.checkUsernameAvailability(customerRegistrationRequestDTO.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        Customer customer = CustomerMapper.INSTANCE.toCustomer(customerRegistrationRequestDTO);
        String rawPassword = PasswordUtil.generateDefaultPassword();
        customer.setPassword(passwordEncoder.encode(rawPassword));
        customer.setIban(IbanUtil.generateIban());
        Customer savedCustomer = databaseService.saveCustomer(customer);
        savedCustomer.setPassword(rawPassword);

        return CustomerMapper.INSTANCE.toCustomerRegistrationResponseDTO(savedCustomer);
    }
}
