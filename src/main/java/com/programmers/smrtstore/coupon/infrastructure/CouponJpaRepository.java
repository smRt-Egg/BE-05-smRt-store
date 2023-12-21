package com.programmers.smrtstore.coupon.infrastructure;

import com.programmers.smrtstore.coupon.domain.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<Coupon,Long> {
}
