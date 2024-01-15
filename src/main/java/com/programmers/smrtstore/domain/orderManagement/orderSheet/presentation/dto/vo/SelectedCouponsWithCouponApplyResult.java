package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class SelectedCouponsWithCouponApplyResult {
    private Map<Long, List<CouponApplyResult>> couponApplyResult;
    private SelectedCoupons selectedCoupons;
}
