package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.DeliveryMethodType;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.application.dto.CreateOrderedProductRequest;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateOrderSheetRequest {

    @NotNull
    private Long ordererId;
    @NotNull
    private List<CreateOrderedProductRequest> orderedProducts;
    @NotNull
    private DeliveryMethodType deliveryMethod;
    @NotNull
    private Integer deliveryFee;

}
