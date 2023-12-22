package com.programmers.smrtstore.core.properties;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다."),
    KEEP_NOT_FOUND_ERROR(NOT_FOUND, "해당 요청에 존재하는 찜이 존재하지 않습니다.");
    private final HttpStatus httpStatus;
    private final String message;

}
