package com.abc.bank.accountmanagement.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Validation error details")
public class ValidationError {

    @Schema(description = "Field that caused the error", example = "username")
    private String field;

    @Schema(description = "Error message for the field", example = "must not be blank")
    private String message;
}
