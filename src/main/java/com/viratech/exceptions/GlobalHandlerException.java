package com.viratech.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MovieNotFoundException.class)
    public ErrorResponse resourceNotFoundHandlerException(MovieNotFoundException ex, HttpServletRequest http){
        LocalDateTime timeStamp = LocalDateTime.now();
        Integer statusCode = HttpStatus.NOT_FOUND.value();
        String message = ex.getMessage();
        String path = http.getRequestURI();
        return new ErrorResponse(
                statusCode,
                message,
                timeStamp,
                path
        );
    }
}
