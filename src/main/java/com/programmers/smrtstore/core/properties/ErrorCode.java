package com.programmers.smrtstore.core.properties;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INCORRECT_PASSWORD(BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    // 404
    USER_NOT_FOUND(NOT_FOUND, "user을 찾을 수 없습니다."),

    // 409
    DUPLICATE_LOGIN_ID(CONFLICT, "이미 존재하는 아이디입니다. 다른 아이디를 이용해 주세요."),

    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
