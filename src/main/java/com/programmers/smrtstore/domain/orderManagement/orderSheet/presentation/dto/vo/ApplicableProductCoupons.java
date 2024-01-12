package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
public class ApplicableProductCoupons {
    // 여기 CouponResponse 대신 List<CouponResponse>가 필요해보임
    private Map<Long, List<CouponResponse>> productCouponListsByOrderedProductId;
    private Map<Long, List<CouponResponse>> productDuplicateCouponListsByOrderedProductId;
}
