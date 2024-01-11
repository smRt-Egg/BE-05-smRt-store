package com.programmers.smrtstore.domain.coupon.infrastructure.facade;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponQuantity;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponRepositoryCustomImpl;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class CouponQuantityFacade {

    private final CouponJpaRepository couponJpaRepository;

    public void decrease(Long couponId) {
        int size = couponJpaRepository.findAll().size();
        CouponQuantity couponQuantity = couponJpaRepository.findCouponQuantity(couponId)
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND));

        couponQuantity.decrease(1);
    }

    public Integer update(Long couponId, Integer value) {
        CouponQuantity couponQuantity = couponJpaRepository.findCouponQuantity(couponId)
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND));

        couponQuantity.update(value);
        return value;

    }

}
