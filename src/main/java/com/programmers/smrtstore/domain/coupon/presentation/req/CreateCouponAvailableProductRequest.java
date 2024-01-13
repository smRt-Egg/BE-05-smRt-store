package com.programmers.smrtstore.domain.coupon.presentation.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCouponAvailableProductRequest {
    private Long productId;
}
