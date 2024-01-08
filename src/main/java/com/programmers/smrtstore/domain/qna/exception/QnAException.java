package com.programmers.smrtstore.domain.qna.exception;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.exception.exceptionClass.CustomException;

public class QnAException extends CustomException {
    public QnAException(ErrorCode errorCode) {
        super(errorCode, errorCode.getMessage());
    }
}
