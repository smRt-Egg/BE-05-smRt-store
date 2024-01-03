package com.programmers.smrtstore.domain.cart.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class CartException extends CustomException {

    public CartException(ErrorCode errorCode) {
        super(errorCode, null);
    }

    public CartException(ErrorCode errorCode,
        String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
