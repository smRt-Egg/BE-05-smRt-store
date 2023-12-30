package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.*;

import com.programmers.smrtstore.domain.product.domain.entity.QProduct;
import com.programmers.smrtstore.domain.user.domain.entity.QUser;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public CouponQuantity findCouponQuantity(Long couponId) {
        QCouponQuantity qCouponQuantity = QCouponQuantity.couponQuantity;
        return queryFactory
                .select(qCouponQuantity)
                .from(qCouponQuantity)
                .where(qCouponQuantity.id.eq(couponId))
                .fetchOne();
    }

    public List<Coupon> findUserCoupons(Long userId) {
        QCouponAvailableUser qCouponAvailableUser = QCouponAvailableUser.couponAvailableUser;
        QCoupon qCoupon = QCoupon.coupon;

        return queryFactory
                .select(qCoupon)
                .from(qCouponAvailableUser)
                .join(qCoupon)
                .on(qCouponAvailableUser.coupon.id.eq(qCoupon.id))
                .where(qCouponAvailableUser.user.id.eq(userId))
                .fetch();
    }

    public Optional<Coupon> findCouponByUserIdAndCouponId(Long userId, Long couponId) {
        QCouponAvailableUser qCouponAvailableUser = QCouponAvailableUser.couponAvailableUser;
        QCoupon qCoupon = QCoupon.coupon;

        Coupon coupon = queryFactory
                .select(qCoupon)
                .from(qCouponAvailableUser)
                .join(qCoupon)
                .on(qCouponAvailableUser.coupon.id.eq(qCoupon.id))
                .where(qCouponAvailableUser.user.id.eq(userId))
                .where(qCouponAvailableUser.coupon.id.eq(couponId))
                .fetchOne();
        return Optional.ofNullable(coupon);
    }

    public Long findUserCouponCount(Long userId) {
        QCouponAvailableUser qCouponAvailableUser = QCouponAvailableUser.couponAvailableUser;
        QCoupon qCoupon = QCoupon.coupon;

        return queryFactory
                .select(qCoupon)
                .from(qCouponAvailableUser)
                .join(qCoupon)
                .on(qCouponAvailableUser.coupon.id.eq(qCoupon.id))
                .where(qCouponAvailableUser.user.id.eq(userId))
                .fetchCount();
    }

}
