package com.programmers.smrtstore.domain.coupon.presentation.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SaveCouponRequest {
    @NotNull
    private final Long couponId;
}
