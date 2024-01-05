package com.programmers.smrtstore.domain.review.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class ReviewException extends CustomException {

    public ReviewException(ErrorCode errorCode) {
        super(errorCode, null);
    }

    public ReviewException(ErrorCode errorCode,
        String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
