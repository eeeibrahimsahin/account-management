package com.abc.bank.accountmanagement.constant;

public class SwaggerConstants {
    public static final String REGISTER_SUCCESS_RESPONSE = """
                {
                    "traceId": "123e4567-e89b-12d3-a456-426614174000",
                    "status": 200,
                    "message": "Customer registered successfully"
                }
            """;

    public static final String REGISTER_INVALID_INPUT_RESPONSE = """
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
            """;

    public static final String UNAUTHORIZED_RESPONSE = """
                {
                    "traceId": "123e4567-e89b-12d3-a456-426614174000",
                    "status": 401,
                    "message": "Unauthorized"
                }
            """;

    public static final String USERNAME_EXISTS_RESPONSE = """
                {
                    "traceId": "123e4567-e89b-12d3-a456-426614174000",
                    "status": 409,
                    "message": "Username already exists"
                }
            """;

    public static final String LOGIN_INVALID_INPUT_RESPONSE = """
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
            """;

    public static final String INVALID_CREDENTIALS_RESPONSE = """
                {
                    "traceId": "123e4567-e89b-12d3-a456-426614174000",
                    "status": 401,
                    "message": "Invalid username or password"
                }
            """;
    public static final String CHECK_USERS_RESPONSE = """
               [
                   {
                       "id": 1,
                       "name": "John Doe",
                       "address": "123 Main St",
                       "dateOfBirth": "1990-01-01",
                       "idDocument": "123456789",
                       "username": "johndoe",
                       "iban": "NL91ABNA0417164300",
                       "password": "******"
                   },
                   {
                       "id": 3,
                       "name": "John Doe",
                       "address": "123 Main St",
                       "dateOfBirth": null,
                       "idDocument": "123456789",
                       "username": "johndoe1",
                       "iban": "NL28ABNA0164312201",
                       "password": "******"
                   }
               ]
            """;
}

