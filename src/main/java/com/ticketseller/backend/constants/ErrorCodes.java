package com.ticketseller.backend.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorCodes {
    public static int NO_SUCH_USER = 1;
    public static int INVALID_CREDENTIALS = 2;
    public static int JWT = 3;
    public static int REQUIRED_ROLES_NOT_PRESENT = 5;
    public static int INTERNAL_SERVER_ERROR = 6;
    public static int NOT_FOUND = 7;
    public static int BAD_REQUEST = 9;
    public static int VALIDATION_FAILED = 10;
    public static int UNAUTHORIZED = 11;
}
