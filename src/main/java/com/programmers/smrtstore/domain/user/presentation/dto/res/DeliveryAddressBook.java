package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import java.util.List;

public class DeliveryAddressBook {

    private ShippingAddress defaultDeliveryAddress;
    private List<ShippingAddress> deliveryAddresses;

}
