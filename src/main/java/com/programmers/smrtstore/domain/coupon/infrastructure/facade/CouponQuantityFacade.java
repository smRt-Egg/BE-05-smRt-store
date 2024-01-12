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

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class CouponQuantityFacade {

    private final CouponJpaRepository couponJpaRepository;

    public void decrease(Long couponId) {

        while (true) {
            try {
                CouponQuantity couponQuantity = couponJpaRepository.findCouponQuantity(couponId)
                                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND, "false"));

                couponQuantity.decrease(1);
                break;
            } catch (OptimisticLockException e) {
                log.info("다운로드 시도중입니다.");
            }
        }
    }

    public Integer update(Long couponId, Integer value) {
        while (true) {
            try {
                CouponQuantity couponQuantity = couponJpaRepository.findCouponQuantity(couponId)
                                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND, "false"));

                couponQuantity.update(value);
                return value;

            } catch (OptimisticLockException e) {
                log.info("쿠폰 수량 업데이트 시도중입니다.");
            }
        }
    }
}
