package com.programmers.smrtstore.domain.coupon.presentation.res;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.BenefitUnitType;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserCouponResponse {

    private final String name;
    private final BenefitUnitType type;
    private final Long discountValue;
    private final Long minOrderPrice;
    private final boolean isDupilcate;
    private final LocalDateTime validEndDate;
    public static UserCouponResponse toDto(Coupon coupon) {
        return new UserCouponResponse(
                coupon.getCouponValue().getName(),
                coupon.getBenefitUnitType(),
                coupon.getCouponValue().getMaxDiscountValue(),
                coupon.getCouponValue().getMinOrderPrice(),
                coupon.isDuplicationYn(),
                coupon.getValidPeriodEndDate());
    }

}
