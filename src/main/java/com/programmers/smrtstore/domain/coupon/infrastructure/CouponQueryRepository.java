package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.QCoupon;

import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import com.programmers.smrtstore.domain.coupon.domain.entity.QCouponAvailableUser;
import com.programmers.smrtstore.domain.user.domain.entity.QUser;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Optional<Coupon> findCouponByIdWithPessimistic(Long id) {
        QCoupon qCoupon = QCoupon.coupon;
        Coupon coupon = queryFactory
                .selectFrom(qCoupon)
                .where(qCoupon.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(coupon);
    }

}
