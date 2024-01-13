package com.programmers.smrtstore.domain.coupon.presentation.vo;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDiscountCoupon implements Comparable<OrderDiscountCoupon>{
    private Coupon coupon;
    private Long orderedProductId;
    private Integer baseAmount;
    private Integer discountAmount;

    @Override
    public int compareTo(OrderDiscountCoupon c) {
        return (this.discountAmount-c.discountAmount);
    }
}
