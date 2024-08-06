package com.abc.bank.accountmanagement.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerOverviewResponseDTO {
    private int id;
    private String name;
    private String address;
    private String dateOfBirth;
    private String idDocument;
    private String username;

    @JsonIgnore
    private String password;
    private String iban;

    @JsonGetter("password")
    public String getMaskedPassword() {
        return "******";
    }
}
