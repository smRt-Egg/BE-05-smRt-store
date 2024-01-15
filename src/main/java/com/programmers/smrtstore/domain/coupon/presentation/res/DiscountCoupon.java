package com.programmers.smrtstore.domain.coupon.presentation.res;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DiscountCoupon implements Comparable<DiscountCoupon> {

    private final Coupon productCoupon;
    private final Coupon cartCoupon;
    private final Integer productCouponDiscount;
    private final Integer orderCouponDiscount;
    private final Integer totalDiscountAmount;

    @Override
    public int compareTo(DiscountCoupon c) {
        return (this.totalDiscountAmount-c.totalDiscountAmount);
    }
}
