package com.programmers.smrtstore.domain.coupon.domain.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class CouponException extends CustomException {


    public CouponException(ErrorCode errorCode) {
        super(errorCode);
    }

    public CouponException(ErrorCode errorCode, String runtimeValue) {
        super(errorCode, runtimeValue);
    }
}
