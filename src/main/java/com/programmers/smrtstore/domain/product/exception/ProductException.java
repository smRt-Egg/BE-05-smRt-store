package com.programmers.smrtstore.domain.product.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class ProductException extends CustomException {

    public ProductException(ErrorCode errorCode,
        String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
