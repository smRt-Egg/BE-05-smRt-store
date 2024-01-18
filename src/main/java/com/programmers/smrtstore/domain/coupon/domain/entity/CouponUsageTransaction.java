package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponStatus;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_usage_transaction_TB")
public class CouponUsageTransaction extends CouponTransaction {

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    private CouponUsageTransaction(User user, Coupon coupon, Order order, CouponStatus couponStatus) {
        this.user = user;
        this.coupon = coupon;
        this.order = order;
        this.couponStatus = couponStatus;
    }
    public static CouponUsageTransaction of(User user, Coupon coupon, Order order, CouponStatus couponStatus) {
        return new CouponUsageTransaction(user, coupon, order, couponStatus);
    }
}
