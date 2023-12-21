package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.domain.coupon.presentation.req.CreateCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import com.programmers.smrtstore.domain.coupon.presentation.res.CreateCouponResponse;

import java.util.List;

public interface AdminCouponService {
    CreateCouponResponse createCoupon(CreateCouponRequest request);

    List<CouponResponse> getCoupons();

    CouponResponse getCouponById(Long id);

    Long blockCouponById(Long id);

    Long addProductToCoupon(Long couponId, List<Long> productList);

    Long addUserToCoupon(Long couponId, List<Long> userList);
}
