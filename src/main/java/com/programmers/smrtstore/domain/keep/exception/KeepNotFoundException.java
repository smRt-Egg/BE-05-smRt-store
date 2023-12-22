package com.programmers.smrtstore.domain.keep.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class KeepNotFoundException extends CustomException {
    public KeepNotFoundException() {
        super(ErrorCode.KEEP_NOT_FOUND_ERROR, ErrorCode.KEEP_NOT_FOUND_ERROR.getMessage());
    }
}
