package com.programmers.smrtstore.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.dto.ErrorResponse;
import com.programmers.smrtstore.exception.dto.ValidationErrorResponse;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(value = {
        BindException.class,
        MethodArgumentNotValidException.class
    })
    protected ResponseEntity<List<ValidationErrorResponse>> validationException(BindException e,
        HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        List<ValidationErrorResponse> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            ValidationErrorResponse error = new ValidationErrorResponse(
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue()
            );
            errors.add(error);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    protected ResponseEntity<ErrorResponse> handleTokenExpiredException(
        TokenExpiredException e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(ErrorCode.SECURITY_TOKEN_EXPIRED,
            e.getMessage());
    }

    @ExceptionHandler(value = {AuthenticationException.class, JWTVerificationException.class})
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(
        AuthenticationException e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(ErrorCode.SECURITY_UNAUTHORIZED,
            e.getMessage());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(
        AccessDeniedException e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(ErrorCode.SECURITY_ACCESS_DENIED,
            e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(
        Exception e, HttpServletRequest request
    ) {
        return ErrorResponse.toResponseEntity(ErrorCode.SERVER_ERROR, e.getMessage());
    }
}
