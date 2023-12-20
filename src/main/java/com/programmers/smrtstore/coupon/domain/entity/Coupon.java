package com.programmers.smrtstore.coupon.domain.entity;

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

    private boolean isMembershipCoupon;

    private boolean isDuplicate;

    private boolean isValid;

    private BenefitUnitType benefitUnitType;

    private CustomerManageBenefitType customerManageBenefitType;

    private CouponPublicationType couponPublicationType;

    private LocalDateTime validPeriodStartDate;

    private LocalDateTime validPeriodEndDate;

    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    private CouponQuantity couponQuantity;
}
