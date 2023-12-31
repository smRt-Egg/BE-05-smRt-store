package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.programmers.smrtstore.domain.coupon.domain.entity.QCoupon.coupon;
import static com.programmers.smrtstore.domain.coupon.domain.entity.QCouponAvailableProduct.couponAvailableProduct;
import static com.programmers.smrtstore.domain.coupon.domain.entity.QCouponAvailableUser.couponAvailableUser;
import static com.programmers.smrtstore.domain.coupon.domain.entity.QCouponQuantity.couponQuantity;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public CouponQuantity findCouponQuantity(Long couponId) {
        return queryFactory
                .select(couponQuantity)
                .from(couponQuantity)
                .where(couponQuantity.id.eq(couponId))
                .fetchOne();
    }

    @Override
    public List<Coupon> findUserCoupons(Long userId) {

        return queryFactory
                .select(coupon)
                .from(couponAvailableUser)
                .join(coupon)
                .on(couponAvailableUser.coupon.id.eq(coupon.id))
                .where(couponAvailableUser.user.id.eq(userId))
                .fetch();
    }

    @Override
    public Optional<Coupon> findCouponByUserIdAndCouponId(Long userId, Long couponId) {

        return Optional.ofNullable(
                queryFactory
                        .select(coupon)
                        .from(couponAvailableUser)
                        .join(coupon)
                        .on(couponAvailableUser.coupon.id.eq(coupon.id))
                        .where(couponAvailableUser.user.id.eq(userId))
                        .where(couponAvailableUser.coupon.id.eq(couponId))
                        .fetchOne());
    }

    @Override
    public Long findUserCouponCount(Long userId) {

        return queryFactory
                .select(coupon.id.count())
                .from(couponAvailableUser)
                .join(coupon)
                .on(couponAvailableUser.coupon.id.eq(coupon.id))
                .where(couponAvailableUser.user.id.eq(userId))
                .fetchOne();
    }

    @Override
    public List<Coupon> findCouponByProductId(Long productId) {
        return queryFactory
                .select(coupon)
                .from(couponAvailableProduct)
                .join(coupon)
                .on(couponAvailableProduct.coupon.id.eq(coupon.id))
                .where(couponAvailableProduct.product.id.eq(productId))
                .fetch();
    }

}
