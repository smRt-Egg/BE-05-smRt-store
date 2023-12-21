package com.programmers.smrtstore.domain.user.exception;

public class LoginIdDuplicationException extends RuntimeException {
    public LoginIdDuplicationException(String message) {
        super(message);
    }
}
