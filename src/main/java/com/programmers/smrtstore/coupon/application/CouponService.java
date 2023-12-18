package com.programmers.smrtstore.coupon.application;

import com.programmers.smrtstore.coupon.presentation.req.OrderCouponRequest;
import com.programmers.smrtstore.coupon.presentation.req.RegisterCouponRequest;
import com.programmers.smrtstore.coupon.presentation.req.SaveCouponRequest;
import com.programmers.smrtstore.coupon.presentation.res.CouponResponse;
import com.programmers.smrtstore.coupon.presentation.res.OrderCouponResponse;
import com.programmers.smrtstore.coupon.presentation.res.RegisterCouponResponse;
import com.programmers.smrtstore.coupon.presentation.res.SaveCouponResponse;

import java.util.List;

public interface CouponService {

    SaveCouponResponse save(SaveCouponRequest request);
    RegisterCouponResponse register(RegisterCouponRequest request);

    List<CouponResponse> getCouponByUserId(Long userId);
    List<CouponResponse> getCouponByProductId(Long productId);

    OrderCouponResponse order(OrderCouponRequest request);
}
