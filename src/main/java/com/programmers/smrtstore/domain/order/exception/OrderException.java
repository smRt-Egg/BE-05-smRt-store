package com.programmers.smrtstore.domain.order.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class OrderException extends CustomException {

    public OrderException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
