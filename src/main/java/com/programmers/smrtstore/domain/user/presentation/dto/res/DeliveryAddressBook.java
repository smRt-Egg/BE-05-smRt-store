package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.user.application.vo.AggUserShippingInfo;
import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryAddressBook {

    private ShippingAddress defaultDeliveryAddress;
    private List<ShippingAddress> deliveryAddresses;

    public static DeliveryAddressBook from(AggUserShippingInfo separated) {
        return new DeliveryAddressBook(separated.getDefaultShippingAddress(),
            separated.getNotDefaultShippingAddresses());
    }
}
