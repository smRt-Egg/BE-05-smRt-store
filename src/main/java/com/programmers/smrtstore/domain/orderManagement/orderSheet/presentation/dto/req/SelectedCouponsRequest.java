package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class SelectedCouponsRequest {
    //Long은 orderedProduct
    private Map<Long, Long> selectedProductCouponListsByOrderedProductId; //오더프로덕트당 선택된 쿠폰
    private Long selectedDeliveryFeeCouponId;
    private Long selectedCartCouponId;
}
