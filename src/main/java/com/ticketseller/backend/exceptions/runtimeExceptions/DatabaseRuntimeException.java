package com.ticketseller.backend.exceptions.runtimeExceptions;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

public class DatabaseRuntimeException extends BaseRuntimeException {

    public DatabaseRuntimeException(String message, int errorCode, HttpStatus httpStatus) {
        super(message, errorCode, httpStatus);
    }

}
