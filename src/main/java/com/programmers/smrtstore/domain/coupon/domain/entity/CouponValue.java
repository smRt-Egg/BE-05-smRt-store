package com.programmers.smrtstore.domain.coupon.domain.entity;

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
    private Long benefitValue;

    @Column(nullable = false)
    private Long maxDiscountValue;

    @Column(nullable = false)
    private Long minOrderPrice;

    @Column(nullable = false)
    private Integer idPerIssuableCount;

    @Builder
    private CouponValue(String name, String content, Long benefitValue, Long maxDiscountValue, Long minOrderPrice, Integer idPerIssuableCount) {
        this.name = name;
        this.content = content;
        this.benefitValue = benefitValue;
        this.maxDiscountValue = maxDiscountValue;
        this.minOrderPrice = minOrderPrice;
        this.idPerIssuableCount = idPerIssuableCount;
    }
}
