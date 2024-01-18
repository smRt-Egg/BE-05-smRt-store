package com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req;

import com.programmers.smrtstore.domain.orderManagement.delivery.entity.DeliveryInfo;
import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateDeliveryAddressRequest {

    private Long shippingAddressId;

    private boolean useDeliveryMemo;

    private String deliveryMemo;

    @Builder
    public CreateDeliveryAddressRequest(
        Long shippingAddressId, boolean useDeliveryMemo, String deliveryMemo
    ) {
        this.shippingAddressId = shippingAddressId;
        this.useDeliveryMemo = useDeliveryMemo;
        this.deliveryMemo = deliveryMemo;
    }

    public DeliveryInfo createDeliveryInfoFrom(ShippingAddress shippingAddress) {
        if (!useDeliveryMemo) {
            this.deliveryMemo = "";
        }
        return DeliveryInfo.builder()
            .address1Depth(shippingAddress.getAddress1Depth())
            .address2Depth(shippingAddress.getAddress2Depth())
            .zipCode(shippingAddress.getZipCode())
            .receiverName(shippingAddress.getRecipient())
            .receiverPhone(shippingAddress.getPhoneNum1())
            .deliveryRequest(deliveryMemo)
            .build();
    }
}
