package com.programmers.smrtstore.core.properties;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 200
    KEEP_NOT_FOUND_ERROR(OK, "찜이 존재하지 않습니다."),
    // 400
    INCORRECT_PASSWORD(BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    PRODUCT_QUANTITY_NOT_ENOUGH(BAD_REQUEST, "상품의 재고가 부족합니다."),
    PRODUCT_ALREADY_RELEASED(BAD_REQUEST, "이미 출시된 상품입니다."),
    PRODUCT_NOT_AVAILABLE(BAD_REQUEST, "상품이 준비중입니다."),
    PRODUCT_ALREADY_AVAILABLE(BAD_REQUEST, "이미 판매중인 상품입니다."),
    PRODUCT_NOT_USE_OPTION(BAD_REQUEST, "옵션을 사용하지 않는 상품입니다."),
    PRODUCT_NOT_RELEASED(BAD_REQUEST, "출시되지 않은 상품입니다."),
    PRODUCT_NOT_DISCOUNTED(BAD_REQUEST, "할인을 진행하지 않은 상품입니다."),
    PRODUCT_DISCOUNT_RATIO_NOT_VALID(BAD_REQUEST, "할인율이 올바르지 않습니다."),
    NON_MEMBERSHIP(BAD_REQUEST, "멤버쉽 유저 전용 쿠폰입니다"),
    COUPON_NOT_ENOUGH(BAD_REQUEST, "쿠폰 재고가 부족합니다."),
    COUPON_DATE_INVALID(BAD_REQUEST, "쿠폰 유효 기간이 끝났습니다."),
    COUPON_EXIST(BAD_REQUEST, "사용 하지 않은 쿠폰이 존재합니다."),
    ISSUE_COUNT_EXCEED(BAD_REQUEST, "인당 쿠폰 발급 횟수를 초과했습니다."),
    COUPON_ALREADY_USED(BAD_REQUEST, "이미 사용 완료한 쿠폰입니다."),
    COUPON_NOT_AVAILABLE(BAD_REQUEST, "유효하지 않은 쿠폰입니다."),
    ORDER_PRICE_NOT_ENOUGH(BAD_REQUEST, "쿠폰의 최소 주문 금액 미만입니다."),
    POINT_ILLEGAL_ARGUMENT(BAD_REQUEST, "포인트 범위가 유효하지 않습니다."),
    CART_QUANTITY_NOT_ENOUGH(BAD_REQUEST, "장바구니의 상품 갯수는 0이하일 수 없습니다."),
    CART_ALREADY_EXIST(BAD_REQUEST, "이미 장바구니에 담긴 상품입니다."),
    // 401
    MISSING_CREDENTIALS(UNAUTHORIZED, "사용자의 인증 정보를 찾을 수 없습니다."),
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    // 403
    SECURITY_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),
    // 404
    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(NOT_FOUND, "주문을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(NOT_FOUND, "상품을 찾을 수 없습니다."),
    PRODUCT_OPTION_NOT_FOUND(NOT_FOUND, "상품 옵션을 찾을 수 없습니다."),
    COUPON_NOT_FOUND(NOT_FOUND, "존재하지 않는 쿠폰번호입니다."),
    TOKEN_NOT_FOUND(NOT_FOUND, "토큰을 찾을 수 없습니다."),
    POINT_NOT_FOUND(NOT_FOUND, "포인트를 찾을 수 없습니다."),
    CART_NOT_FOUND(NOT_FOUND, "장바구니를 찾을 수 없습니다."),
    // 409
    DUPLICATE_USERNAME(CONFLICT, "이미 존재하는 아이디입니다. 다른 아이디를 이용해 주세요."),
    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
