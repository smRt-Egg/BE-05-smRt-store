package com.programmers.smrtstore.exception;

import com.programmers.smrtstore.exception.dto.ErrorResponse;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<ErrorResponse> handleCustomException(
        CustomException e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(e.getErrorCode(), e.getRuntimeValue());
    }
}
