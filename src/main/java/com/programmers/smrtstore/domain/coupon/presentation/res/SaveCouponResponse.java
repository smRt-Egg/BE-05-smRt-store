package com.programmers.smrtstore.domain.coupon.presentation.res;

import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveCouponResponse {
    private final Long couponId;
    public static SaveCouponResponse toDto(CouponAvailableUser couponAvailableUser){
        return new SaveCouponResponse(couponAvailableUser.getCoupon().getId());
    }
}
