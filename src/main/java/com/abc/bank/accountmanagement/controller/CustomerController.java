package com.abc.bank.accountmanagement.controller;

import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.dto.LoginRequestDTO;
import com.abc.bank.accountmanagement.exception.AuthenticationException;
import com.abc.bank.accountmanagement.exception.ErrorResponse;
import com.abc.bank.accountmanagement.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Validated
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;
    private AuthenticationManager authenticationManager;

    @Operation(summary = "Register a new customer", description = "Registers a customer with provided details and returns the username and random password upon success.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerRegistrationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "traceId": "123e4567-e89b-12d3-a456-426614174000",
                        "status": 400,
                        "message": "Validation failed",
                        "errors": [
                            {
                                "field": "name",
                                "message": "Name is mandatory"
                            },
                            {
                                "field": "address",
                                "message": "Address is mandatory"
                            }
                        ]
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "traceId": "123e4567-e89b-12d3-a456-426614174000",
                        "status": 401,
                        "message": "Unauthorized"
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "Username already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "traceId": "123e4567-e89b-12d3-a456-426614174000",
                        "status": 409,
                        "message": "Username already exists"
                    }
                    """)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerRegistrationRequestDTO customerRegistrationRequestDTO) {
        CustomerRegistrationResponseDTO registeredCustomer = customerService.register(customerRegistrationRequestDTO);
        return ResponseEntity.ok(registeredCustomer);
    }

    @PostMapping("/logon")
    @Operation(summary = "User login", description = "Allows a user to log in with username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "traceId": "123e4567-e89b-12d3-a456-426614174000",
                        "status": 400,
                        "message": "Validation failed",
                        "errors": [
                            {
                                "field": "username",
                                "message": "must not be blank"
                            },
                            {
                                "field": "password",
                                "message": "must not be blank"
                            }
                        ]
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = """
                    {
                        "traceId": "123e4567-e89b-12d3-a456-426614174000",
                        "status": 401,
                        "message": "Invalid username or password"
                    }
                    """)))
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
            return ResponseEntity.ok("Login successful");
        } catch (Exception exception) {
            throw new AuthenticationException("Invalid username or password");
        }
    }
}

