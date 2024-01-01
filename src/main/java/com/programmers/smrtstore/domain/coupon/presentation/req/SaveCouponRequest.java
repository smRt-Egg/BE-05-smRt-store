package com.programmers.smrtstore.domain.coupon.presentation.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveCouponRequest {
    private final Long couponId;
}
