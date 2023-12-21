package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.domain.coupon.presentation.req.OrderCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.req.SaveCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.req.UpdateUserCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.res.OrderCouponResponse;
import com.programmers.smrtstore.domain.coupon.presentation.res.ProductCouponResponse;
import com.programmers.smrtstore.domain.coupon.presentation.res.SaveCouponResponse;
import com.programmers.smrtstore.domain.coupon.presentation.res.UserCouponResponse;
import java.util.List;

public interface CouponService {

    SaveCouponResponse save(SaveCouponRequest request);
    List<UserCouponResponse> getCouponsByUserId(Long userId);
    UserCouponResponse getCouponById(Long couponId);
    ProductCouponResponse getCouponByProductIdAndUserId(Long productId, Long userId);
    OrderCouponResponse calculateWithCoupon(OrderCouponRequest request);
    Long updateUserCoupon(Long userId, UpdateUserCouponRequest request );
}
