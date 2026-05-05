package com.example.babybee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import com.example.babybee.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<?> handleAccessDeniedException(AccessDeniedException ex) {
        return ApiResponse.builder()
                .success(false)
                .message("Access Denied: You do not have permission to access this resource.")
                .data(null)
                .build();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleRuntimeException(RuntimeException ex) {

        return ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .data(null)
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleException(Exception ex) {

        return ApiResponse.builder()
                .success(false)
                .message("Something went wrong")
                .data(null)
                .build();
    }
}
