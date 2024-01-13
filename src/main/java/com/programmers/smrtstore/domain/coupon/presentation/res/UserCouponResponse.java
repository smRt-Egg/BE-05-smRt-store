package com.programmers.smrtstore.domain.coupon.presentation.res;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.BenefitUnitType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class UserCouponResponse {

    private final String name;
    private final String content;
    private final BenefitUnitType benefitUnitType;
    private final Integer benefitValue;
    private final Integer maxDiscountValue;
    private final Integer minOrderPrice;
    private final LocalDateTime validPeriodEndDate;

    public static UserCouponResponse from(Coupon coupon) {
        return UserCouponResponse.builder()
                .name(coupon.getCouponValue().getName())
                .content(coupon.getCouponValue().getContent())
                .benefitUnitType(coupon.getBenefitUnitType())
                .benefitValue(coupon.getCouponValue().getBenefitValue())
                .maxDiscountValue(coupon.getCouponValue().getMaxDiscountValue())
                .minOrderPrice(coupon.getCouponValue().getMinOrderPrice())
                .validPeriodEndDate(coupon.getValidPeriodEndDate())
                .build();

    }
}
