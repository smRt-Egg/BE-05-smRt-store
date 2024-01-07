package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.CouponCommonTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponCommonTransactionJpaRepository extends JpaRepository<CouponCommonTransaction, Long> {
}
