package com.ticketseller.backend.controllers;

import com.ticketseller.backend.constants.ErrorCodes;
import com.ticketseller.backend.exceptions.runtimeExceptions.BaseRuntimeException;
import com.ticketseller.backend.exceptions.runtimeExceptions.ServerRuntimeException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) throws Exception {
        Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        if (exception instanceof BaseRuntimeException) {
            throw exception;
        }

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status == null)
            throw new ServerRuntimeException("Server error.", ErrorCodes.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

        int statusCode = Integer.parseInt(status.toString());

        String message = "ServerRuntimeException: " + HttpStatus.resolve(statusCode).getReasonPhrase();

        throw new ServerRuntimeException(message, statusCode, HttpStatus.resolve(statusCode));
    }
}