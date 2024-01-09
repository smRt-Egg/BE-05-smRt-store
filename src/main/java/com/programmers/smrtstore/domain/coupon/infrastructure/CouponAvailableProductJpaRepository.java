package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponAvailableProductJpaRepository extends JpaRepository<CouponAvailableProduct,Long> {
    Optional<CouponAvailableProduct> findByCouponIdAndProductId(Long couponId, Long productId);

    void deleteByCouponIdAndProductId(Long couponId, Long productId);

}
