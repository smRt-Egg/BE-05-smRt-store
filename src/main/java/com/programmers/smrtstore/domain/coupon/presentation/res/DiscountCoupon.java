package com.programmers.smrtstore.domain.coupon.presentation.res;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DiscountCoupon implements Comparable<DiscountCoupon> {

    private final Coupon coupon;
    private final Long discount;

    @Override
    public int compareTo(DiscountCoupon c) {
        return (int)(this.discount-c.discount);
    }
}
