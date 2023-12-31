package com.programmers.smrtstore.domain.coupon.infrastructure.facade;

import com.programmers.smrtstore.domain.coupon.domain.entity.CouponQuantity;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponRepositoryCustomImpl;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class CouponQuantityFacade {

    private final CouponRepositoryCustomImpl couponRepositoryCustomImpl;

    public void decrease(Long couponId)  {

        while (true) {
            try {
                CouponQuantity couponQuantity = couponRepositoryCustomImpl.findCouponQuantity(couponId);
                couponQuantity.decrease(1);
                break;
            } catch (OptimisticLockException e) {
                log.info("다운로드 시도중입니다.");
            }
        }
    }
}
