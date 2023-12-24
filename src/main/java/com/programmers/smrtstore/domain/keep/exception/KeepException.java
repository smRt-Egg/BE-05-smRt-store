package com.programmers.smrtstore.domain.keep.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class KeepException extends CustomException {
    public KeepException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
