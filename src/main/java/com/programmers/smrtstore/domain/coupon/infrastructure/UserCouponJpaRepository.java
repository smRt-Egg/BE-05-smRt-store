package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon,Long> {
}
