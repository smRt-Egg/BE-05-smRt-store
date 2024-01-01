package com.programmers.smrtstore.domain.coupon.presentation.res;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponQuantity;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.BenefitUnitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponPublicationType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CustomerManageBenefitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.vo.CouponValue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponResponse {

    private final String name;
    private final String content;
    private final Long benefitValue;
    private final Long maxDiscountValue;
    private final Long minOrderPrice;
    private final Integer idPerIssuableCount;
    private final Boolean membershipCouponYn;
    private final Boolean duplicationYn;
    private final Boolean availableYn;
    private final CouponType couponType;
    private final BenefitUnitType benefitUnitType;
    private final CustomerManageBenefitType customerManageBenefitType;
    private final CouponPublicationType couponPublicationType;
    private final LocalDateTime validPeriodStartDate;
    private final LocalDateTime validPeriodEndDate;
    private final Integer quantity;

    public static CouponResponse of(Coupon coupon) {
        return CouponResponse.builder()
                .name(coupon.getCouponValue().getName())
                .content(coupon.getCouponValue().getContent())
                .benefitValue(coupon.getCouponValue().getBenefitValue())
                .maxDiscountValue(coupon.getCouponValue().getMaxDiscountValue())
                .minOrderPrice(coupon.getCouponValue().getMinOrderPrice())
                .idPerIssuableCount(coupon.getCouponValue().getIdPerIssuableCount())
                .membershipCouponYn(coupon.isMembershipCouponYn())
                .duplicationYn(coupon.isDuplicationYn())
                .availableYn(coupon.isAvailableYn())
                .couponType(coupon.getCouponType())
                .benefitUnitType(coupon.getBenefitUnitType())
                .customerManageBenefitType(coupon.getCustomerManageBenefitType())
                .couponPublicationType(coupon.getCouponPublicationType())
                .validPeriodStartDate(coupon.getValidPeriodStartDate())
                .validPeriodEndDate(coupon.getValidPeriodEndDate())
                .quantity(coupon.getCouponQuantity().getValue())
                .build();

    }
}
