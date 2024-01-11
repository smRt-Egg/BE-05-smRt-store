package com.programmers.smrtstore.domain.coupon.presentation.res;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderSheetCouponResponse {
    private Integer baseAmount;
    private Integer discountAmount;
    private CouponType couponType;
    private Boolean duplicationYn;
}
