package com.abc.bank.accountmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response body for registered user")
public class CustomerRegistrationResponseDTO {
    @Schema(description = "Username for login", example = "johndoe")
    private String username;
    @Schema(description = "Password for login", example = "password")
    private String password;
    private String iban;
}
