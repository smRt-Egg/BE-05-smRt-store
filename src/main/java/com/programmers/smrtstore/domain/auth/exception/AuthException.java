package com.programmers.smrtstore.domain.auth.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(ErrorCode errorCode,
        String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
