package com.programmers.smrtstore.domain.user.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class UserException extends CustomException {

    public UserException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
