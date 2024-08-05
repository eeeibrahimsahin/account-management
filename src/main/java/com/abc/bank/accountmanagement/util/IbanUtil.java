package com.abc.bank.accountmanagement.util;


import java.security.SecureRandom;

public class IbanUtil {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String COUNTRY_CODE = "NL";
    private static final String BANK_CODE = "ABNA";

    public static String generateIban() {
        String checkDigits = String.format("%02d", RANDOM.nextInt(100));
        String accountNumber = String.format("%010d", RANDOM.nextInt(1000000000));
        return COUNTRY_CODE + checkDigits + BANK_CODE + accountNumber;
    }
}
