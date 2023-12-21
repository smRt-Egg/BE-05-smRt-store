package com.programmers.smrtstore.coupon.infrastructure;

import com.programmers.smrtstore.coupon.domain.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon,Long> {
}
