package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
public class OrderSheetCouponInfo {
    //여기서 Long은 OrderedProduct Id
    private Map<Long, List<CouponApplyResult>> discountsByOptionId; // Orderedproduct에 대한 Long, 쿠폰 적용 결과 리스트
    private SelectedCoupons selectedCoupons; //선택된 쿠폰에 대한 정보
    private ApplicableProductCoupons productCoupons;
    private ApplicableDeliveryFeeCoupons deliveryFeeCoupons;
    private List<CouponResponse> cartCoupons;
    // 해당 주문서에 적용 할 수 있는 유효 한 할인 횟수 (즉시할인 포함)
    private Integer usableProductAndStoreDiscountCount;
    // 해당 주문서에 적용 할 수 있는 전체 쿠폰 갯수 (카트+상품 , 즉시할인 제외)
    private Integer validProductAndStoreCouponCount;
}
