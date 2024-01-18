package com.programmers.smrtstore.domain.coupon.presentation.req;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponQuantity;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.BenefitUnitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponPublicationType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CustomerManageBenefitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.vo.CouponValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateCouponRequest {

    @NotBlank
    private final String name;
    @NotBlank
    private final String content;
    @NotNull
    @Min(value = 1,message = "쿠폰 할인의 최소값은 1 이상입니다.")
    private final Integer benefitValue;
    @NotNull
    @Min(value = 1,message = "쿠폰의 최대할인값은 1 이상입니다.")
    private final Integer maxDiscountValue;
    @NotNull
    private final Integer minOrderPrice;
    @NotNull
    @Min(value = 0,message = "인당 쿠폰 발급 횟수는 0회 이상입니다.")
    private final Integer idPerIssuableCount;
    @NotNull
    private final Boolean membershipCouponYn;
    @NotNull
    private final Boolean duplicationYn;
    @NotNull
    private final Boolean availableYn;
    @NotNull
    private final CouponType couponType;
    @NotNull
    private final BenefitUnitType benefitUnitType;
    @NotNull
    private final CustomerManageBenefitType customerManageBenefitType;
    @NotNull
    private final CouponPublicationType couponPublicationType;
    @NotNull
    private final LocalDateTime validPeriodStartDate;
    @NotNull
    private final LocalDateTime validPeriodEndDate;
    @NotNull
    private final Integer quantity;

    public static Coupon toEntity(CreateCouponRequest request) {

        return Coupon.builder()
                .couponValue(
                        CouponValue.builder()
                                .name(request.name)
                                .content(request.name)
                                .benefitValue(request.benefitValue)
                                .minOrderPrice(request.minOrderPrice)
                                .maxDiscountValue(request.maxDiscountValue)
                                .idPerIssuableCount(request.idPerIssuableCount)
                                .build()
                )
                .membershipCouponYn(request.membershipCouponYn)
                .duplicationYn(request.duplicationYn)
                .availableYn(request.availableYn)
                .couponPublicationType(request.couponPublicationType)
                .couponType(request.couponType)
                .benefitUnitType(request.benefitUnitType)
                .customerManageBenefitType(request.customerManageBenefitType)
                .validPeriodStartDate(request.validPeriodStartDate)
                .validPeriodEndDate(request.validPeriodEndDate)
                .couponQuantity(CouponQuantity.from(request.quantity))
                .build();
    }

}

