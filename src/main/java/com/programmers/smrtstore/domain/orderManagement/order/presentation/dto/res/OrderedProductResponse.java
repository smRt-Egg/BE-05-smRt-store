package com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res;

import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderedProductResponse {

    private Long orderedProductId;

    private ProductResponse product;

    private Integer quantity;

    private Integer totalPrice;

    @Builder
    private OrderedProductResponse(
        Long orderedProductId, ProductResponse product, Integer quantity, Integer totalPrice
    ) {
        this.orderedProductId = orderedProductId;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public static OrderedProductResponse from(OrderedProduct orderedProduct) {
        return OrderedProductResponse.builder()
            .orderedProductId(orderedProduct.getId())
            .product(ProductResponse.from(orderedProduct.getProduct()))
            .quantity(orderedProduct.getQuantity())
            .totalPrice(orderedProduct.getTotalPrice())
            .build();
    }

}
