package com.programmers.smrtstore.domain.user.application;

import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DefaultSeparateResult {

    private ShippingAddress defaultShippingAddress;
    private List<ShippingAddress> notDefaultShippingAddresses;
}
