package com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.SelectedCouponsRequest;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateOrderRequest {

    private Long orderSheetId;

    private CreateDeliveryAddressRequest deliveryAddress;

    // TODO: 결제 도메인을 만든 후
    // private CreatePaymentRequest payment;

    // 이걸로 예상포인트까지
    private SelectedCouponsRequest selectedCoupons;

    private Integer usedPoint;

    @Builder
    public CreateOrderRequest(
        Long orderSheetId, CreateDeliveryAddressRequest deliveryAddress,
        SelectedCouponsRequest selectedCoupons
    ) {
        this.orderSheetId = orderSheetId;
        this.deliveryAddress = deliveryAddress;
        this.selectedCoupons = selectedCoupons;
    }
}
