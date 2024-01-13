package com.programmers.smrtstore.domain.coupon.presentation.vo;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartDiscountCoupon implements Comparable<CartDiscountCoupon>{
    private Coupon coupon;
    private Integer discountValue;
    @Override
    public int compareTo(CartDiscountCoupon o) {
        return this.discountValue-o.discountValue;
    }
}
