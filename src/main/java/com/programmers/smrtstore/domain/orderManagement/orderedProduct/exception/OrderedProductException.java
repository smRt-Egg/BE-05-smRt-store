package com.programmers.smrtstore.domain.orderManagement.orderedProduct.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class OrderedProductException extends CustomException {

    public OrderedProductException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderedProductException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
