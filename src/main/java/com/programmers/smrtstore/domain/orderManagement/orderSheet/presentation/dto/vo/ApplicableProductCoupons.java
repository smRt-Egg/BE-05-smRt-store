package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import java.util.Map;

public class ApplicableProductCoupons {

    private Map<Long, CouponResponse> productCouponListsByOrderSheetOptionId;
    private Map<Long, CouponResponse> productDuplicateCouponListsByOrderSheetOptionId;
}
