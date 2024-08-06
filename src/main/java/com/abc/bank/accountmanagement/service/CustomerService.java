package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.dto.CustomerOverviewResponseDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerOverviewResponseDTO> getAll();

    public CustomerRegistrationResponseDTO register(CustomerRegistrationRequestDTO customerRegistrationRequestDTO);
}
