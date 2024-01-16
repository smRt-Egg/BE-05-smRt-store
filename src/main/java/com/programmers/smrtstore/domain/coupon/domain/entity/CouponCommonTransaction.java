package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponStatus;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_common_transaction_TB")
public class CouponCommonTransaction extends CouponTransaction {

    private CouponCommonTransaction(User user, Coupon coupon, CouponStatus couponStatus) {
        this.user = user;
        this.coupon = coupon;
        this.couponStatus = couponStatus;
    }

    public static CouponCommonTransaction of(User user, Coupon coupon, CouponStatus couponStatus) {
        return new CouponCommonTransaction(user, coupon, couponStatus);
    }

}
