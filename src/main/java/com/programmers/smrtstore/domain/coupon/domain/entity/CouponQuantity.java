package com.programmers.smrtstore.domain.coupon.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CouponQuantity {

    @Id
    @Column(name = "coupon_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Coupon coupon;

    private Integer value;
}
