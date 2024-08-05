package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.exception.UsernameNotFoundException;
import com.abc.bank.accountmanagement.model.Customer;
import com.abc.bank.accountmanagement.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private DatabaseService databaseService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = databaseService.findCustomerByUsername(username);
        return new User(customer.getUsername(), customer.getPassword(), new ArrayList<>());
    }
}
