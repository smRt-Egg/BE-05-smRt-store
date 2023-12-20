package com.programmers.smrtstore.coupon.application;

import com.programmers.smrtstore.coupon.presentation.req.OrderCouponRequest;
import com.programmers.smrtstore.coupon.presentation.req.SaveCouponRequest;
import com.programmers.smrtstore.coupon.presentation.req.UpdateUserCouponRequest;
import com.programmers.smrtstore.coupon.presentation.res.*;

import java.util.List;

public interface CouponService {

    SaveCouponResponse save(SaveCouponRequest request);
    List<UserCouponResponse> getCouponsByUserId(Long userId);
    UserCouponResponse getCouponById(Long couponId);
    ProductCouponResponse getCouponByProductIdAndUserId(Long productId, Long userId);
    OrderCouponResponse calculateWithCoupon(OrderCouponRequest request);
    Long updateUserCoupon(Long userId, UpdateUserCouponRequest request );
}
