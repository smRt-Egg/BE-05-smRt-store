package com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;

// orderSheet 에서 해당 product 에 적용 가능한, 유저가 가진 쿠폰 정보
public class OrderSheetDiscountedProductInfo {
    private CouponType couponType;
    private Integer baseAmount;
    private Integer discountAmount;

}
