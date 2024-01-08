package com.programmers.smrtstore.domain.user.presentation.dto.req;

import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DetailShippingRequest {

    @Size(max = 10, message = "배송지 이름은 10자 이하여야 합니다.")
    private String name;

    @NotBlank
    @Size(min = 1, max = 10, message = "수령인 이름은 1~10자여야 합니다.")
    private String recipient;

    @NotBlank
    @Size(min = 1, max = 50, message = "주소는 1~50자여야 합니다.")
    private String address1Depth;

    @NotBlank
    @Size(min = 1, max = 30, message = "상세 주소는 1~30자여야 합니다.")
    private String address2Depth;

    @NotBlank
    @Size(min = 1, max = 10, message = "우편 번호는 1~10자여야 합니다.")
    private String zipCode;

    @NotBlank
    @Size(min = 1, max = 15, message = "전화번호는 15자 이내여야 합니다.")
    private String phoneNum1;

    @Size(max = 15, message = "전화번호는 15자 이내여야 합니다.")
    private String phoneNum2;

    @NotNull
    private Boolean defaultYn;

    public ShippingAddress toShippingAddressEntity(User user) {
        return ShippingAddress.builder()
            .defaultYn(defaultYn)
            .phoneNum2(phoneNum2)
            .phoneNum1(phoneNum1)
            .zipCode(zipCode)
            .address2Depth(address2Depth)
            .address1Depth(address1Depth)
            .name(name)
            .recipient(recipient)
            .user(user)
            .build();
    }
}
