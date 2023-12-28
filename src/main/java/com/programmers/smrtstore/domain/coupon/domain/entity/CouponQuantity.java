package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "coupon_quantity_TB")
public class CouponQuantity {

    @Id
    @Column(nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY,mappedBy = "couponQuantity")
    @MapsId
    private Coupon coupon;

    @Column(nullable = false)
    private Integer value;

    @Version
    private Integer version;

    private CouponQuantity(Integer value) {
        this.value = value;
    }

    public static CouponQuantity from(Integer value) {
        return new CouponQuantity(value);
    }


    public void increase(int num) {
        value+=num;
    }

    public void decrease(int num) {
        if (value < num) {
            throw new CouponException(ErrorCode.COUPON_NOT_ENOUGH, String.valueOf(value));
        }
        value-=num;
    }
}
