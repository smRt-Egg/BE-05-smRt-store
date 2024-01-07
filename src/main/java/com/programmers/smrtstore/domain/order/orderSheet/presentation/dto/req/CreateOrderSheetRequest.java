package com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.req;

import com.programmers.smrtstore.domain.order.domain.entity.enums.DeliveryMethodType;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateOrderSheetRequest {

    private Long ordererId;

    private List<CreateOrderedProductRequest> orderedProducts;

    private DeliveryMethodType deliveryMethod;

}
