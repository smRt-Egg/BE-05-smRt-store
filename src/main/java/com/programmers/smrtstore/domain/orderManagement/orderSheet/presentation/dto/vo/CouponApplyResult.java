package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CouponApplyResult {

    private Long couponId;
    private CouponType couponType;
    private Integer baseAmount;
    private Integer discountAmount;
}
