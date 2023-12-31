package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponAvailableProductJpaRepository extends JpaRepository<CouponAvailableProduct,Long> {
}
