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
    PRODUCT_USE_OPTION(BAD_REQUEST, "옵션을 사용하는 상품입니다."),
    PRODUCT_USE_SINGLE_OPTION(BAD_REQUEST, "단일 옵션을 사용하는 상품입니다."),
    PRODUCT_NOT_RELEASED(BAD_REQUEST, "출시되지 않은 상품입니다."),
    PRODUCT_NOT_DISCOUNTED(BAD_REQUEST, "할인을 진행하지 않은 상품입니다."),
    PRODUCT_DISCOUNT_RATIO_NOT_VALID(BAD_REQUEST, "할인율이 올바르지 않습니다."),
    PRODUCT_OPTION_TYPE_INVALID(BAD_REQUEST, "잘못된 옵션 타입입니다."),
    PRODUCT_OPTION_NAME_TYPE_INVALID(BAD_REQUEST, "잘못된 옵션 이름입니다."),
    PRODUCT_OPTION_NAME_INVALID(BAD_REQUEST, "잘못된 옵션입니다."),
    PRODUCT_OPTION_MISMATCH(BAD_REQUEST, "상품에 존재하지 않는 옵션입니다."),
    NON_MEMBERSHIP(BAD_REQUEST, "멤버쉽 유저 전용 쿠폰입니다"),
    COUPON_NOT_ENOUGH(BAD_REQUEST, "쿠폰 재고가 부족합니다."),
    COUPON_DATE_INVALID(BAD_REQUEST, "쿠폰 유효 기간이 끝났습니다."),
    COUPON_EXIST(BAD_REQUEST, "사용 하지 않은 쿠폰이 존재합니다."),
    ISSUE_COUNT_EXCEED(BAD_REQUEST, "인당 쿠폰 발급 횟수를 초과했습니다."),
    COUPON_ALREADY_USED(BAD_REQUEST, "이미 사용 완료한 쿠폰입니다."),
    COUPON_NOT_AVAILABLE(BAD_REQUEST, "유효하지 않은 쿠폰입니다."),
    COUPON_STOCK_INVALID(BAD_REQUEST,"쿠폰 수량은 0개 이상이어야 합니다."),
    ORDER_PRICE_NOT_ENOUGH(BAD_REQUEST, "쿠폰의 최소 주문 금액 미만입니다."),
    REVIEW_ALREADY_EXIST(BAD_REQUEST, "이미 리뷰를 작성하였습니다."),
    REVIEW_LIKE_ALREADY_EXIST(BAD_REQUEST, "이미 리뷰를 좋아요 하였습니다."),
    REVIEW_NOT_EXIST_WHEN_NOT_ORDER_PRODUCT(BAD_REQUEST, "주문하지 않은 상품에 대한 리뷰는 작성할 수 없습니다."),
    POINT_ILLEGAL_ARGUMENT(BAD_REQUEST, "포인트 범위가 유효하지 않습니다."),
    POINT_AVAILABLE_RANGE_EXCEED(BAD_REQUEST, "한 번에 사용 가능한 포인트 한도를 초과했습니다."),
    CART_QUANTITY_NOT_ENOUGH(BAD_REQUEST, "장바구니의 상품 갯수는 0이하일 수 없습니다."),
    CART_ALREADY_EXIST(BAD_REQUEST, "이미 장바구니에 담긴 상품입니다."),
    CART_PRODUCT_DETAIL_OPTION_NOT_MATCH(BAD_REQUEST, "유효하지 않은 품목과 옵션 매칭입니다."),
    COUPON_PERCENT_EXCEED(BAD_REQUEST,"쿠폰의 퍼센트 할인값은 100% 를 초과할 수 없습니다."),
    EXCEEDED_MAXIMUM_NUMBER_OF_SHIPPING_ADDRESS(BAD_REQUEST, "배송지 수가 이미 최대입니다."),
    DEFAULT_SHIPPING_NOT_DELETABLE(BAD_REQUEST, "다른 배송지를 기본 배송지로 변경 후 삭제해주세요."),
    CART_REQUEST_USER_MISMATCH(BAD_REQUEST, "장바구니 유저와 일치하지 않는 유저 정보입니다."),
    INVALID_AGE(BAD_REQUEST, "나이는 7~200살이어야 합니다."),
    INVALID_NICKNAME_LENGTH(BAD_REQUEST, "별명은 1~10자여야 합니다."),
    INVALID_EMAIL_FORM(BAD_REQUEST, "이메일이 형식에 맞지 않습니다."),
    INVALID_BIRTH_FORM(BAD_REQUEST, "생년월일이 형식에 맞지 않습니다."),
    INVALID_PHONE_NUM_FORM(BAD_REQUEST, "전화번호가 형식에 맞지 않습니다."),
    INVALID_SHIPPING_ADDRESS_NAME_LENGTH(BAD_REQUEST, "배송지 이름은 1~10자여야 합니다."),
    INVALID_RECIPIENT_LENGTH(BAD_REQUEST, "수령인 이름은 1~10자여야 합니다."),
    INVALID_ADDRESS_1_DEPTH_LENGTH(BAD_REQUEST, "주소의 길이는 1~50자여야 합니다."),
    INVALID_ADDRESS_2_DEPTH_LENGTH(BAD_REQUEST, "상세 주소의 길이는 1~30자여야 합니다."),
    // 401
    MISSING_CREDENTIALS(UNAUTHORIZED, "사용자의 인증 정보를 찾을 수 없습니다."),
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    // 403
    SECURITY_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),
    // 404
    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다."),
    QUESTION_NOT_FOUND(NOT_FOUND, "문의를 찾을 수 없습니다"),
    ANSWER_NOT_FOUND(NOT_FOUND, "답변을 찾을 수 없습니다."),
    ORDER_NOT_FOUND(NOT_FOUND, "주문을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(NOT_FOUND, "상품을 찾을 수 없습니다."),
    PRODUCT_OPTION_NOT_FOUND(NOT_FOUND, "상품 옵션을 찾을 수 없습니다."),
    PRODUCT_CATEGORY_NOT_FOUND(NOT_FOUND, "상품 카테고리를 찾을 수 없습니다."),
    COUPON_NOT_FOUND(NOT_FOUND, "존재하지 않는 쿠폰번호입니다."),
    TOKEN_NOT_FOUND(NOT_FOUND, "토큰을 찾을 수 없습니다."),
    POINT_NOT_FOUND(NOT_FOUND, "포인트를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    REVIEW_LIKE_NOT_FOUND(NOT_FOUND, "리뷰 좋아요를 찾을 수 없습니다."),
    CART_NOT_FOUND(NOT_FOUND, "장바구니를 찾을 수 없습니다."),
    SHIPPING_ADDRESS_NOT_FOUND(NOT_FOUND, "배송지를 찾을수 없습니다."),
    // 409
    DUPLICATE_USERNAME(CONFLICT, "이미 존재하는 아이디입니다. 다른 아이디를 이용해 주세요."),
    DUPLICATE_SHIPPING_ADDRESS(CONFLICT, "동일한 배송지가 존재합니다. 수정 후 다시 시도해주세요."),
    // 500
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
