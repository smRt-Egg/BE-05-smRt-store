package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: NoArgsConstructor 쿠폰 완성 후 삭제 예정
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class OrderSheetCouponInfo {
    //여기서 Long은 OrderedProduct Id
    private Map<Long, List<CouponApplyResult>> discountsByOrderedProductId; // OrderedProduct 에 대한 Long, 쿠폰 적용 결과 리스트
    private SelectedCoupons selectedCoupons; //선택된 쿠폰에 대한 정보
    private ApplicableProductCoupons productCoupons;
    private ApplicableDeliveryFeeCoupons deliveryFeeCoupons;
    private List<CouponResponse> cartCoupons;
}
