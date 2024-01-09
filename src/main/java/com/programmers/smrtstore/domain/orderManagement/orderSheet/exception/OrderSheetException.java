package com.programmers.smrtstore.domain.orderManagement.orderSheet.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class OrderSheetException extends CustomException {

    public OrderSheetException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderSheetException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
