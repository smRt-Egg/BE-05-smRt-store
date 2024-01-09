package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponQuantity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface CouponRepositoryCustom {
    Optional<CouponQuantity> findCouponQuantity(Long couponId);
    List<Coupon> findUserCoupons(Long userId);
    Optional<Coupon> findCouponByUserIdAndCouponId(Long userId, Long couponId);
    Long findUserCouponCount(Long userId);
    List<Coupon> findCouponByProductId(Long productId);
    void updateExpiredCoupons();
}
