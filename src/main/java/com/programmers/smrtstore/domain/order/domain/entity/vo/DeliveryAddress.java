package com.programmers.smrtstore.domain.order.domain.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryAddress {

    @Column(name = "address_1depth")
    private String address1depth;

    @Column(name = "address_2depth")
    private String address2depth;

    @Column(name = "zip_code")
    private String zipCode;
}
