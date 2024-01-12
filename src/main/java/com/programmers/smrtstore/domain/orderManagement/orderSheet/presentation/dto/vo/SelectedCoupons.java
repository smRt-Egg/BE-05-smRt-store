package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import java.util.List;
import java.util.Map;


public class SelectedCoupons {
    //여기서 Long은 OrderedProductId .
    private Map<Long, CouponResponse> selectedProductCouponListsByOrderedProductId; //오더프로덕트당 선택된 쿠폰
    private Map<Long, CouponResponse> selectedProductDuplicateCouponsByOrderedProductId;
    private List<CouponResponse> selectedCartCoupons;
}
