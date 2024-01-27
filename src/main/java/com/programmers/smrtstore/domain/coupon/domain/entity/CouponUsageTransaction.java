package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponStatus;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_usage_transaction_TB")
public class CouponUsageTransaction extends CouponTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    private OrderedProduct orderedProduct;

    private CouponUsageTransaction(User user, Coupon coupon, Order order,OrderedProduct orderedProduct, CouponStatus couponStatus) {
        this.user = user;
        this.coupon = coupon;
        this.order = order;
        this.orderedProduct = orderedProduct;
        this.couponStatus = couponStatus;
    }
    public static CouponUsageTransaction of(User user, Coupon coupon, Order order,OrderedProduct orderedProduct, CouponStatus couponStatus) {
        return new CouponUsageTransaction(user, coupon, order, orderedProduct,couponStatus);
    }
}
