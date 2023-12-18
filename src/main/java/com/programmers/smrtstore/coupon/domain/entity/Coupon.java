package com.programmers.smrtstore.coupon.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    private String content;

    private BenefitUnitType benefitUnitType;

    private Long benefitValue;

    private CustomerManageBenefitType customerManageBenefitType;

    private CouponPublicationType couponPublicationType;

    private Long maxDiscountValue;

    private Long minOrderAmount;

    private Integer idPerIssuableCount;

    private Integer unusedCouponCount;

    private boolean isMembershipCoupon;

    private boolean isDuplicate;

    private boolean isValid;

    private LocalDateTime validPeriodStartDate;

    private LocalDateTime validPeriodEndDate;

    private LocalDateTime createdAt;
}
