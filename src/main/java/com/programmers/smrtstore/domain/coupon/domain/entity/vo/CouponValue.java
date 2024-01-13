package com.programmers.smrtstore.domain.coupon.domain.entity.vo;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponValue {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer benefitValue;

    @Column(nullable = false)
    private Integer maxDiscountValue;

    @Column(nullable = false)
    private Integer minOrderPrice;

    @Column(nullable = false)
    private Integer idPerIssuableCount;

    @Builder
    private CouponValue(String name, String content, Integer benefitValue, Integer maxDiscountValue, Integer minOrderPrice, Integer idPerIssuableCount) {
        validateBenefitValueAndMaxDiscountValue(benefitValue, maxDiscountValue);
        this.name = name;
        this.content = content;
        this.benefitValue = benefitValue;
        this.maxDiscountValue = maxDiscountValue;
        this.minOrderPrice = minOrderPrice;
        this.idPerIssuableCount = idPerIssuableCount;
    }

    private void validateBenefitValueAndMaxDiscountValue(Integer benefitValue, Integer maxDiscountValue) {
        if (benefitValue > maxDiscountValue) {
            throw new CouponException(ErrorCode.COUPON_BENEFIT_VALUE_EXCEED);
        }
    }
}
