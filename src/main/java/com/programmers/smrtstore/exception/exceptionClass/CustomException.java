package com.programmers.smrtstore.exception.exceptionClass;

import com.programmers.smrtstore.core.properties.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String runtimeValue;
}
