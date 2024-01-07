package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailOptionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDetailOptionPriceResponse {

    private Long id;
    private Integer price;

    public static ProductDetailOptionPriceResponse from(ProductDetailOptionResponse response) {
        return new ProductDetailOptionPriceResponse(
            response.getId(),
            response.getPrice()
        );
    }
}
