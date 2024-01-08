package com.programmers.smrtstore.domain.coupon.infrastructure;

import com.programmers.smrtstore.domain.coupon.domain.entity.CouponUsageTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUsageTransactionJpaRepository extends JpaRepository<CouponUsageTransaction,Long> {
}
