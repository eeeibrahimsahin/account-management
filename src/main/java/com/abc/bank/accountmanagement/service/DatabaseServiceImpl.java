package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.exception.UsernameNotFoundException;
import com.abc.bank.accountmanagement.model.Customer;
import com.abc.bank.accountmanagement.repository.CustomerRepository;
import com.abc.bank.accountmanagement.exception.TooManyRequestsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DatabaseServiceImpl implements DatabaseService {

    private final RateLimiterService rateLimiterService;
    private final CustomerRepository customerRepository;

    public Customer saveCustomer(Customer customer) {
        rateLimitCheck();
        return customerRepository.save(customer);
    }

    public Customer findCustomerByUsername(String username) {
        rateLimitCheck();
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean checkUsernameAvailability(String username) {
        rateLimitCheck();
        return customerRepository.findByUsername(username).isEmpty();
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    private void rateLimitCheck() {
        if (!rateLimiterService.tryConsume()) {
            throw new TooManyRequestsException("Too many requests - please try again later");
        }
    }
}

