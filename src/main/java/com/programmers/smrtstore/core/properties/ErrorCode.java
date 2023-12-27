package com.programmers.smrtstore.core.properties;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다."),
    // 200
    KEEP_NOT_FOUND_ERROR(OK, "찜이 존재하지 않습니다."),

    NOT_MEMBERSHIP(OK, "멤버쉽 유저 전용 쿠폰입니다"),
    COUPON_NOT_ENOUGH(OK, "쿠폰 재고가 부족합니다."),
    COUPON_DATE_INVALID(OK, "쿠폰 유효 기간이 끝났습니다."),
    COUPON_EXIST(OK, "사용 하지 않은 쿠폰이 존재합니다."),
    ISSUE_COUNT_EXCEED(OK, "인당 쿠폰 발급 횟수를 초과했습니다."),
    COUPON_ALREADY_USE(OK, "이미 사용 완료한 쿠폰입니다."),
    COUPON_NOT_AVAILABLE(OK, "유효하지 않은 쿠폰입니다."),
    ORDER_PRICE_NOT_ENOUGH(OK, "쿠폰의 최소 주문 금액 미만입니다."),
    COUPON_NOT_FOUND(NOT_FOUND, "존재하지 않는 쿠폰번호입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
