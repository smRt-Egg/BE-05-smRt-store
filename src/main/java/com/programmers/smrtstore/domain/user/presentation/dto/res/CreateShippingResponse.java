package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateShippingResponse {

    private String name;

    private String recipient;

    private String address1Depth;

    private String address2Depth;

    private String zipCode;

    private String phoneNum1;

    private String phoneNum2;

    private boolean isDefaultYn;

    public static CreateShippingResponse from(ShippingAddress address) {
        return CreateShippingResponse.builder()
            .name(address.getName())
            .recipient(address.getRecipient())
            .address1Depth(address.getAddress1Depth())
            .address2Depth(address.getAddress2Depth())
            .zipCode(address.getZipCode())
            .phoneNum1(address.getPhoneNum1())
            .phoneNum2(address.getPhoneNum2())
            .isDefaultYn(address.isDefaultYn())
            .build();
    }
}
