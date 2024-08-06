package com.abc.bank.accountmanagement.util;

import java.util.Base64;

public class TokenUtil {
    public static String generateToken(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}

