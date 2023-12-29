package com.programmers.smrtstore.domain.coupon.presentation;

import com.programmers.smrtstore.domain.coupon.application.UserCouponService;
import com.programmers.smrtstore.domain.coupon.presentation.req.SaveCouponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CouponApiController {

    private final UserCouponService couponService;

    @PostMapping("/product/{userId}/coupons")
    public void save(@RequestAttribute(value = "userId") Long userId, @PathVariable Long id,
                     @RequestBody SaveCouponRequest request) {
        couponService.save(request,userId);
    }
}
