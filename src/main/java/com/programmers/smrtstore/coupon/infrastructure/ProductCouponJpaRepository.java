package com.programmers.smrtstore.coupon.infrastructure;

import com.programmers.smrtstore.coupon.domain.entity.ProductCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCouponJpaRepository extends JpaRepository<ProductCoupon,Long> {
}
