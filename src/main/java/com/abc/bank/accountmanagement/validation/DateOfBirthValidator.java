package com.abc.bank.accountmanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateOfBirthValidator implements ConstraintValidator<ValidDateOfBirth, String> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(ValidDateOfBirth constraintAnnotation) {
    }

    @Override
    public boolean isValid(String dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null || dateOfBirth.isBlank()) {
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(dateOfBirth, DATE_FORMATTER);
            return date.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

