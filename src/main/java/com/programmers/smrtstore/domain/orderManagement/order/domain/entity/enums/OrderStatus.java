package com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums;

import java.util.List;

public enum OrderStatus {
    PAYMENT_WAITING, // 결제 대기
    PAYMENT_COMPLETED, // 결제 완료
    DELIVERY_PREPARING, // 상품 준비중
    DELIVERING_BEFORE, // 배송 준비중
    DELIVERING, // 배송중
    DELIVERED, // 배송 완료
    CANCELLED, // 주문 취소
    PURCHASE_CONFIRMED, // 구매 확정
    REFUND_REQUESTED, // 환불 요청
    REFUND_COMPLETED, // 환불 완료
    CANCELLED_BY_NO_PAYMENT, // 미결제 주문 취소
    DISPATCH_DELAYED // 배송 지연
    ;

    public static List<OrderStatus> getActiveOrderStatus() {
        return List.of(
            PAYMENT_WAITING,
            PAYMENT_COMPLETED,
            DELIVERY_PREPARING,
            DELIVERING_BEFORE,
            DELIVERING,
            DELIVERED,
            DISPATCH_DELAYED
        );
    }
}
