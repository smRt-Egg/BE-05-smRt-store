package com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo;

public class ApplicableDeliveryFeeCoupons {

    private Long selectedCouponId;
    // 현재 유저가 가진 쿠폰 중, 해당 주문에서 사용 가능한 배송비 쿠폰의 개수
    private Integer usableDeliveryFeeCouponCount;
}
