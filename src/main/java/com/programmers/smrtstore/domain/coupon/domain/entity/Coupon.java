package com.programmers.smrtstore.domain.coupon.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "coupon_TB")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String content;

    private Long benefitValue;

    private Long maxDiscountValue;

    private Long minOrderAmount;

    private Integer idPerIssuableCount;

    private boolean membershipCouponYn;

    private boolean duplicationYn;

    private boolean availableYn;
    @Enumerated(EnumType.STRING)
    private CouponType couponType;
    @Enumerated(EnumType.STRING)
    private BenefitUnitType benefitUnitType;
    @Enumerated(EnumType.STRING)
    private CustomerManageBenefitType customerManageBenefitType;
    @Enumerated(EnumType.STRING)
    private CouponPublicationType couponPublicationType;

    private LocalDateTime validPeriodStartDate;

    private LocalDateTime validPeriodEndDate;

    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private CouponQuantity couponQuantity;
}
