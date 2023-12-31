package com.programmers.smrtstore.domain.coupon.presentation.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCouponResponse {
    private List<UserCouponResponse>issuableCoupons;
    private List<UserCouponResponse>unIssuableCoupons;
    private List<DiscountCoupon> maxDiscountCoupons;
    public static ProductCouponResponse of(List<UserCouponResponse> issuableCoupons, List<UserCouponResponse> unIssuableCoupons, List<DiscountCoupon> maxDiscountCoupons) {
        return new ProductCouponResponse(issuableCoupons, unIssuableCoupons, maxDiscountCoupons);
    }
}
