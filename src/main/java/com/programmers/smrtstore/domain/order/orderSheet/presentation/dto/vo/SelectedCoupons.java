package com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import java.util.List;
import java.util.Map;


public class SelectedCoupons {

    private Map<Long, CouponResponse> selectedProductCouponListsByOrderSheetOptionId;
    private Map<Long, CouponResponse> selectedProductDuplicateCouponsByOrderSheetItemId;
    private List<CouponResponse> selectedCartCoupons;
}
