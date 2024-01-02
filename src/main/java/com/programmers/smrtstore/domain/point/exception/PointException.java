package com.programmers.smrtstore.domain.point.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class PointException extends CustomException {

    public PointException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PointException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }

}
