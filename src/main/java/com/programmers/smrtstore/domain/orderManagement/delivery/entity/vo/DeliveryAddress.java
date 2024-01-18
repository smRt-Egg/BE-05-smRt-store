package com.programmers.smrtstore.domain.orderManagement.delivery.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryAddress {
    // TODO 대문자로 변경
    @Column(name = "address_1depth")
    private String address1depth;

    @Column(name = "address_2depth")
    private String address2depth;

    @Column(name = "zip_code")
    private String zipCode;

    public DeliveryAddress(String address1depth, String address2depth, String zipCode) {
        this.address1depth = address1depth;
        this.address2depth = address2depth;
        this.zipCode = zipCode;
    }
}
