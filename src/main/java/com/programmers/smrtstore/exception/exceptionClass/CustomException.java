package com.programmers.smrtstore.exception.exceptionClass;

import com.programmers.smrtstore.core.properties.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String runtimeValue;

    public CustomException(ErrorCode errorCode) {
        this(errorCode, "runtimeValue가 존재 하지 않습니다.");
    }

    public CustomException(ErrorCode errorCode, String runtimeValue) {
        this.errorCode = errorCode;
        this.runtimeValue = runtimeValue;
    }
}
