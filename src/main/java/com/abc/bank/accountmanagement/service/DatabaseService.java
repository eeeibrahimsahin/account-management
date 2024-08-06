package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.model.Customer;

import java.util.List;

public interface DatabaseService {
    public Customer saveCustomer(Customer customer);

    public Customer findCustomerByUsername(String username);

    public boolean checkUsernameAvailability(String username);

    public List<Customer> getAll();
}
