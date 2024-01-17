package com.programmers.smrtstore.core.properties;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400
    KEEP_NOT_FOUND_ERROR(BAD_REQUEST, "찜이 존재하지 않습니다."),
    AUTH_INCORRECT_PASSWORD(BAD_REQUEST, "비밀번호가 잘못되었습니다."),
    PRODUCT_QUANTITY_NOT_ENOUGH(BAD_REQUEST, "상품의 재고가 부족합니다."),
    PRODUCT_ALREADY_RELEASED(BAD_REQUEST, "이미 출시된 상품입니다."),
    PRODUCT_NOT_AVAILABLE(BAD_REQUEST, "상품이 준비중입니다."),
    PRODUCT_ALREADY_AVAILABLE(BAD_REQUEST, "이미 판매중인 상품입니다."),
    PRODUCT_NOT_USE_COMBINATION_OPTION(BAD_REQUEST, "병합 옵션을 사용하지 않는 상품입니다."),
    PRODUCT_USE_COMBINATION_OPTION(BAD_REQUEST, "병합 옵션을 사용하는 상품입니다."),
    PRODUCT_NOT_RELEASED(BAD_REQUEST, "출시되지 않은 상품입니다."),
    PRODUCT_NOT_DISCOUNTED(BAD_REQUEST, "할인을 진행하지 않은 상품입니다."),
    PRODUCT_DISCOUNT_RATIO_NOT_VALID(BAD_REQUEST, "할인율이 올바르지 않습니다."),
    PRODUCT_OPTION_NAME_TYPE_INVALID(BAD_REQUEST, "잘못된 옵션명 입니다."),
    PRODUCT_OPTION_NAME_INVALID(BAD_REQUEST, "잘못된 옵션입니다."),
    PRODUCT_OPTION_MISMATCH(BAD_REQUEST, "상품에 존재하지 않는 옵션입니다."),
    COUPON_MEMBERSHIP_USER_ONLY(BAD_REQUEST, "멤버쉽 유저 전용 쿠폰입니다"),
    COUPON_NOT_ENOUGH(BAD_REQUEST, "쿠폰 재고가 부족합니다."),
    COUPON_DATE_INVALID(BAD_REQUEST, "쿠폰 유효 기간이 끝났습니다."),
    COUPON_EXIST_BY_USER(BAD_REQUEST, "유저에게 사용 하지 않은 쿠폰이 존재합니다."),
    COUPON_ISSUE_COUNT_EXCEED(BAD_REQUEST, "인당 쿠폰 발급 횟수를 초과했습니다."),
    COUPON_ALREADY_USED(BAD_REQUEST, "이미 사용 완료한 쿠폰입니다."),
    COUPON_NOT_AVAILABLE(BAD_REQUEST, "유효하지 않은 쿠폰입니다."),
    COUPON_STOCK_INVALID(BAD_REQUEST,"쿠폰 수량은 0개 이상이어야 합니다."),
    // TODO: DELIVERY_FEE_COUPON_ALREADY_APPLIED 통합 논의 필요
    COUPON_ALREADY_APPLIED_PRODUCT(BAD_REQUEST,"이미 쿠폰이 적용된 상품입니다"),
    COUPON_BENEFIT_VALUE_EXCEED(BAD_REQUEST,"쿠폰의 할인값은 최대할인값을 초과할 수 없습니다."),
    COUPON_PRICE_NOT_ENOUGH(BAD_REQUEST,"쿠폰의 최수 주문 금액 미만입니다."),
    COUPON_ONLY_APPLIED_PRODUCT(BAD_REQUEST,"상품에 쿠폰적용은 상품 쿠폰 타입만 가능합니다." ),
    REVIEW_ALREADY_EXIST(BAD_REQUEST, "이미 리뷰를 작성하였습니다."),
    REVIEW_LIKE_ALREADY_EXIST(BAD_REQUEST, "이미 리뷰를 좋아요 하였습니다."),
    REVIEW_NOT_EXIST_WHEN_NOT_ORDER_PRODUCT(BAD_REQUEST, "주문하지 않은 상품에 대한 리뷰는 작성할 수 없습니다."),
    POINT_ILLEGAL_ARGUMENT(BAD_REQUEST, "포인트 범위가 유효하지 않습니다."),
    POINT_AVAILABLE_RANGE_EXCEED(BAD_REQUEST, "한 번에 사용 가능한 포인트 한도를 초과했습니다."),
    CART_QUANTITY_NOT_ENOUGH(BAD_REQUEST, "장바구니의 상품 갯수는 0이하일 수 없습니다."),
    CART_ALREADY_EXIST(BAD_REQUEST, "이미 장바구니에 담긴 상품입니다."),
    COUPON_PERCENT_EXCEED(BAD_REQUEST, "쿠폰의 퍼센트 할인값은 100% 를 초과할 수 없습니다."),
    SHIPPING_ADDRESS_NUMBER_EXCEEDED_MAXIMUM(BAD_REQUEST, "배송지 수가 이미 최대입니다."),
    SHIPPING_ADDRESS_DEFAULT_NOT_DELETABLE(BAD_REQUEST, "다른 배송지를 기본 배송지로 변경 후 삭제해주세요."),
    CART_REQUEST_USER_MISMATCH(BAD_REQUEST, "장바구니 유저와 일치하지 않는 유저 정보입니다."),
    USER_INPUT_INVALID(BAD_REQUEST, "잘못된 User 파라미터 입력입니다."),
    ORDERED_PRODUCT_MISMATCH_ERROR(BAD_REQUEST, "주문한 물건의 정보가 데이터베이스 물건 정보와 일치하지 않습니다."),
    ORDERED_PRODUCT_OPTION_MISMATCH_ERROR(BAD_REQUEST,
        "주문한 물건의 옵션 정보가 데이터베이스 물건 옵션 정보와 일치하지 않습니다."),
    PRODUCT_DETAIL_OPTION_NOT_MATCH(BAD_REQUEST, "유효하지 않은 품목과 옵션 매칭입니다."),
    ORDERED_PRODUCT_QUANTITY_INVALID(BAD_REQUEST, "주문한 물건의 수량이 유효하지 않습니다."),
    ORDERED_PRODUCT_TOTAL_PRICE_INVALID(BAD_REQUEST, "주문한 물건의 총 가격이 유효하지 않습니다."),
    ORDERED_PRODUCT_ORG_PRICE_INVALID(BAD_REQUEST, "주문한 물건의 원가격이 유효하지 않습니다."),
    ORDERED_PRODUCT_IMMEDIATE_DISCOUNT_INVALID(BAD_REQUEST, "주문한 물건의 즉시 할인율이 유효하지 않습니다."),
    ORDERED_PRODUCT_COUPON_DISCOUNT_INVALID(BAD_REQUEST, "주문한 물건의 쿠폰 할인 금액이 유효하지 않습니다."),
    ORDERED_PRODUCT_EXTRA_PRICE_INVALID(BAD_REQUEST, "주문한 물건의 추가 금액이 유효하지 않습니다."),
    DELIVERY_FEE_INVALID(BAD_REQUEST, "배송비가 유효하지 않습니다."),
    PRODUCT_NOT_AVAILABLE_ORDER(BAD_REQUEST, "주문할 수 없는 상품입니다."),
    UPDATED_POINT_VALUE_INVALID(BAD_REQUEST, "갱신할 포인트의 값이 유효하지 않습니다."),
    ORDERSHEET_ALREADY_ORDERED(BAD_REQUEST, "이미 주문이 완료된 주문서입니다."),
    DELIVERY_FEE_COUPON_ALREADY_APPLIED(BAD_REQUEST, "이미 적용된 배송비 쿠폰입니다."),
    // TODO: 적용 할 수 없는 쿠폰 에러 메시지 통합 논의 필요
    DELIVERY_FEE_COUPON_NOT_APPLICABLE(BAD_REQUEST, "배송비가 없는 상품입니다."),
    // 401
    AUTH_MISSING_CREDENTIALS(UNAUTHORIZED, "사용자의 인증 정보를 찾을 수 없습니다."),
    SECURITY_UNAUTHORIZED(UNAUTHORIZED, "인증 정보가 유효하지 않습니다"),
    SECURITY_TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    EMAIL_VERIFICATION_CODE_ERROR(UNAUTHORIZED, "인증 코드가 올바르지 않습니다."),
    // 403
    INVALID_USER(FORBIDDEN, "유효하지 않은 사용자입니다."),
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
    ORDERSHEET_NOT_FOUND(NOT_FOUND, "주문서를 찾을 수 없습니다."),
    // 409
    USER_DUPLICATE_USERNAME(CONFLICT, "이미 존재하는 아이디입니다. 다른 아이디를 이용해 주세요."),
    USER_DUPLICATE_SHIPPING_ADDRESS(CONFLICT, "동일한 배송지가 존재합니다. 수정 후 다시 시도해주세요."),
    USER_DUPLICATE_EMAIL(CONFLICT, "이미 가입되어 있는 이메일입니다."),
    // 500
    EMAIL_SENDING_ERROR(INTERNAL_SERVER_ERROR, "본인 인증을 위한 이메일 전송에 실패하였습니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "예상치 못한 서버 에러가 발생하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
