package com.programmers.smrtstore.domain.orderManagement.delivery;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class DeliveryException extends CustomException {

    public DeliveryException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DeliveryException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
