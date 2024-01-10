package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailShippingResponse {

    private Long id;

    private String name;

    private String recipient;

    private String address1Depth;

    private String address2Depth;

    private String zipCode;

    private String phoneNum1;

    private String phoneNum2;

    private Boolean isDefaultYn;

    public static DetailShippingResponse from(ShippingAddress address) {
        return DetailShippingResponse.builder()
            .id(address.getId())
            .name(address.getName())
            .recipient(address.getRecipient())
            .address1Depth(address.getAddress1Depth())
            .address2Depth(address.getAddress2Depth())
            .zipCode(address.getZipCode())
            .phoneNum1(address.getPhoneNum1())
            .phoneNum2(address.getPhoneNum2())
            .isDefaultYn(address.getDefaultYn())
            .build();
    }
}
