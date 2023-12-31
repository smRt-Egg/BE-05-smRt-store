package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponAvailableUserJpaRepository extends JpaRepository<CouponAvailableUser,Long> {

    Optional<CouponAvailableUser> findByCouponIdAndUserId(Long couponId, Long userId);
}
