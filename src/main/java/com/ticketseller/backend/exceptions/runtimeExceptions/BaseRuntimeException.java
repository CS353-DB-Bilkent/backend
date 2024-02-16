package com.ticketseller.backend.exceptions.runtimeExceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseRuntimeException extends RuntimeException {

    private final int errorCode;
    private final HttpStatus httpStatus;

    public BaseRuntimeException(String message, int errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

}
