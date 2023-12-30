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



}
