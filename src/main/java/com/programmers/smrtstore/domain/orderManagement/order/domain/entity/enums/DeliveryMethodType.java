package com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums;

import static com.programmers.smrtstore.core.properties.ErrorCode.DELIVERY_FEE_INVALID;

import com.programmers.smrtstore.domain.orderManagement.delivery.DeliveryException;
import com.programmers.smrtstore.domain.orderManagement.delivery.DeliveryProperties;
import java.util.function.Consumer;

public enum DeliveryMethodType {
    VISIT(deliveryFee -> {
        if (deliveryFee != 0) {
            throw new DeliveryException(DELIVERY_FEE_INVALID);
        }
    }),
    DELIVERY(deliveryFee -> {
        if (deliveryFee != 0 && deliveryFee != DeliveryProperties.STORE_DELIVERY_FEE) {
            throw new DeliveryException(DELIVERY_FEE_INVALID);
        }
    }),
    ;

    private final Consumer<Integer> validateDeliveryFee;

    DeliveryMethodType(Consumer<Integer> validateDeliveryFee) {
        this.validateDeliveryFee = validateDeliveryFee;
    }

    public void validateDeliveryFee(Integer deliveryFee) {
        validateDeliveryFee.accept(deliveryFee);
    }

}
