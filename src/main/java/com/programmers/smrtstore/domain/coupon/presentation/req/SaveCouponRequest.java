package com.programmers.smrtstore.domain.coupon.presentation.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaveCouponRequest {
    @NotNull
    private Long couponId;
}
