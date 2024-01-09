package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import java.util.List;
import java.util.Map;

public class OrderSheetCouponInfo {

    private Map<Long, List<CouponApplyResult>> discountsByOptionId;
    private SelectedCoupons selectedCoupons;
    private ApplicableProductCoupons productCoupons;
    private ApplicableDeliveryFeeCoupons deliveryFeeCoupons;
    private List<CouponResponse> cartCoupons;
    // 해당 주문서에 적용 할 수 있는 유효 한 할인 횟수 (즉시할인 포함)
    private Integer usableProductAndStoreDiscountCount;
    // 해당 주문서에 적용 할 수 있는 전체 쿠폰 갯수 (카트+상품 , 즉시할인 제외)
    private Integer validProductAndStoreCouponCount;
}
