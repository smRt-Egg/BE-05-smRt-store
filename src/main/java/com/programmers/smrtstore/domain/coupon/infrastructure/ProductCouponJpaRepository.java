package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.ProductCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCouponJpaRepository extends JpaRepository<ProductCoupon,Long> {
}
