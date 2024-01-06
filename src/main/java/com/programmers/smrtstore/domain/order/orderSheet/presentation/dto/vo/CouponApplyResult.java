package com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;

public class CouponApplyResult {

    private Long couponId;
    private CouponType couponType;
    private Integer baseAmount;
    private Integer discountAmount;
}
