package com.abc.bank.accountmanagement.service;

import com.abc.bank.accountmanagement.exception.UsernameNotFoundException;
import com.abc.bank.accountmanagement.model.Customer;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = UserDetailsServiceImpl.class)
class UserDetailsServiceImplTest {

    @MockBean
    private DatabaseService databaseService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Should return UserDetails when user exists")
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        String username = "testuser";
        String password = "password";
        Customer customer = Customer.builder()
                .username(username)
                .password(password)
                .build();
        given(databaseService.findCustomerByUsername(username)).willReturn(customer);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        String username = "nonexistentuser";
        given(databaseService.findCustomerByUsername(username)).willThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }

}