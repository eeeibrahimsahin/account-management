package com.abc.bank.accountmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Request body for registration")
public class CustomerRegistrationRequestDTO {
    @NotBlank(message = "Name is mandatory")
    @Schema(description = "Name of the customer", example = "John Doe")
    private String name;

    @NotBlank(message = "Address is mandatory")
    @Schema(description = "Address of the customer", example = "123 Main St")
    private String address;

    @NotBlank(message = "Date of birth is mandatory")
    @Schema(description = "Date of birth of the customer", example = "1990-01-01")
    private String dateOfBirth;

    @NotBlank(message = "ID document number is mandatory")
    @Schema(description = "ID document of the customer", example = "123456789")
    private String idDocumentNumber;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 20, message = "Username must be less than 20 characters")
    @Schema(description = "Username for login", example = "johndoe")
    private String username;

}
