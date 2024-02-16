package com.ticketseller.backend.exceptions.runtimeExceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AdminRuntimeException extends BaseRuntimeException {
    public AdminRuntimeException(String message, int errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }
}
