package com.programmers.smrtstore.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginIdDuplicationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ResponseEntity<String> loginIdDuplicationException(Exception e) {
        return ResponseEntity.status(409)
            .body(e.getMessage());
    }
}
