package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.*;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.programmers.smrtstore.domain.coupon.domain.entity.QCoupon.coupon;
import static com.programmers.smrtstore.domain.coupon.domain.entity.QCouponAvailableProduct.couponAvailableProduct;
import static com.programmers.smrtstore.domain.coupon.domain.entity.QCouponAvailableUser.couponAvailableUser;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryCustomImpl implements CouponRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

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

    //유저가 보유하면서 상품에 적용되어있는 쿠폰 리스트 반환
    @Override
    public List<Coupon> findCouponByUserIdAndProductId(Long userId, Long productId) {

        return queryFactory
                        .select(coupon)
                        .from(couponAvailableUser)
                        .join(couponAvailableProduct)
                        .on(couponAvailableUser.coupon.id.eq(couponAvailableProduct.coupon.id))
                        .where(couponAvailableUser.user.id.eq(userId))
                        .where(couponAvailableProduct.product.id.eq(productId))
                        .fetch();
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

    @Override
    public void updateExpiredCoupons() {
        queryFactory
                .update(coupon)
                .set(coupon.availableYn, false)
                .where(coupon.availableYn.isTrue().and(coupon.validPeriodEndDate.after(LocalDateTime.now())))
                .execute();
        em.flush();
        em.clear();
    }

    @Override
    public List<Coupon> getCartCoupons() {
        return queryFactory
                .selectFrom(coupon)
                .where(coupon.couponType.eq(CouponType.CART))
                .fetch();
    }

    @Override
    public List<Coupon> getDeliveryFeeCoupons() {
        return queryFactory
                .selectFrom(coupon)
                .where(coupon.couponType.eq(CouponType.DELIVERY))
                .orderBy(coupon.validPeriodEndDate.asc())
                .fetch();
    }
}
