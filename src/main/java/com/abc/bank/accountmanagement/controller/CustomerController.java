package com.abc.bank.accountmanagement.controller;

import com.abc.bank.accountmanagement.dto.CustomerOverviewResponseDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationRequestDTO;
import com.abc.bank.accountmanagement.dto.CustomerRegistrationResponseDTO;
import com.abc.bank.accountmanagement.dto.LoginRequestDTO;
import com.abc.bank.accountmanagement.exception.AuthenticationException;
import com.abc.bank.accountmanagement.exception.ErrorResponse;
import com.abc.bank.accountmanagement.service.CustomerService;
import com.abc.bank.accountmanagement.util.TokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import static com.abc.bank.accountmanagement.constant.SwaggerConstants.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Validated
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {

    private final CustomerService customerService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Register a new customer", description = "Registers a customer with provided details and returns the username and random password upon success.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerRegistrationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = REGISTER_INVALID_INPUT_RESPONSE))),
            @ApiResponse(responseCode = "409", description = "Username already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = USERNAME_EXISTS_RESPONSE)))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerRegistrationRequestDTO customerRegistrationRequestDTO) {
        CustomerRegistrationResponseDTO registeredCustomer = customerService.register(customerRegistrationRequestDTO);
        return ResponseEntity.ok(registeredCustomer);
    }

    @PostMapping("/logon")
    @Operation(summary = "User login", description = "Allows a user to log in with username and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content,
                    headers = @Header(name = "Set-Cookie", description = "Contains the token for authenticated user",
                            schema = @Schema(type = "string", example = "token=eyJhbGciOiJIUzI1NiIsInR5cCI..."))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = LOGIN_INVALID_INPUT_RESPONSE))),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = INVALID_CREDENTIALS_RESPONSE)))
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
            // Generate a token for the authenticated user and put into Cookie to use for login - HTTP Basic Authentication
            response.addCookie(new Cookie("token", TokenUtil.generateToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())));
            return ResponseEntity.ok("Login successful");
        } catch (Exception exception) {
            throw new AuthenticationException("Invalid username or password");
        }
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerOverviewResponseDTO[].class), examples = @ExampleObject(value = CHECK_USERS_RESPONSE))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = "{ \"message\": \"Unauthorized access\" }")))
    })
    @GetMapping(path = "/overview")
    public ResponseEntity<?> checkUsers(@Parameter(description = "Authentication token", required = true, example = "Basic dXNlcm5hbWU6cGFzc3dvcmQ=")
                                        @RequestHeader("Authorization") String authorizationHeader) {
        return new ResponseEntity<>(customerService.getAll(), HttpStatus.OK);
    }
}

