package com.abc.bank.accountmanagement.mapper;

import com.abc.bank.accountmanagement.dto.CustomerOverviewResponseDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    CustomerRegistrationResponseDTO toCustomerRegistrationResponseDTO(Customer customer);

    @Mapping(target = "idDocument", source = "idDocumentNumber")
    Customer toCustomer(CustomerRegistrationRequestDTO customerRegistrationRequestDTO);

    @Mapping(target = "password", ignore = true)
    CustomerOverviewResponseDTO customerToCustomerOverviewResponseDTO(Customer customer);

    List<CustomerOverviewResponseDTO> customersToCustomerOverviewResponseDTOs(List<Customer> customers);

}
