package com.programmers.smrtstore.coupon.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductCoupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private  Long productId;

    private Long couponId;
}
