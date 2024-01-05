package com.programmers.smrtstore.domain.user.application.vo;

import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AggUserShippingInfo {

    private ShippingAddress defaultShippingAddress;
    private List<ShippingAddress> notDefaultShippingAddresses;

    public static AggUserShippingInfo of(ShippingAddress shippingAddress,
        List<ShippingAddress> shippingAddresses) {
        return new AggUserShippingInfo(shippingAddress, shippingAddresses);
    }
}
