package com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.vo;

import static com.programmers.smrtstore.core.properties.ErrorCode.DELIVERY_FEE_COUPON_ALREADY_APPLIED;
import static com.programmers.smrtstore.core.properties.ErrorCode.DELIVERY_FEE_COUPON_NOT_APPLICABLE;

import com.programmers.smrtstore.domain.orderManagement.delivery.DeliveryException;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.DeliveryMethodType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryOptions {

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethodType deliveryMethod;

    @Column(name = "delivery_fee")
    private Integer deliveryFee;

    @Column(name = "applied_delivery_fee_coupon")
    private boolean appliedDeliveryFeeCoupon;

    public DeliveryOptions(DeliveryMethodType deliveryMethod, Integer deliveryFee) {
        this.deliveryMethod = deliveryMethod;
        this.deliveryFee = deliveryFee;
        this.appliedDeliveryFeeCoupon = false;
        validateDeliveryMethodAndFee();
    }

    public void useDeliveryFeeCoupon() {
        if (this.deliveryMethod == DeliveryMethodType.VISIT) {
            throw new DeliveryException(DELIVERY_FEE_COUPON_NOT_APPLICABLE);
        }
        if (this.appliedDeliveryFeeCoupon) {
            throw new DeliveryException(DELIVERY_FEE_COUPON_ALREADY_APPLIED);
        }
        if (this.deliveryFee == 0) {
            throw new DeliveryException(DELIVERY_FEE_COUPON_NOT_APPLICABLE);
        }
        this.appliedDeliveryFeeCoupon = true;
        this.deliveryFee = 0;
    }

    private void validateDeliveryMethodAndFee() {
        this.deliveryMethod.validateDeliveryFee(this.deliveryFee);
    }
}
