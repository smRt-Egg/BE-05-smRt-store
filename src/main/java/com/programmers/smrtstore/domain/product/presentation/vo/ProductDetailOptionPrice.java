package com.programmers.smrtstore.domain.product.presentation.vo;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailOptionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDetailOptionPrice {

    private final Long id;
    private final Integer price;

    public static ProductDetailOptionPrice from(ProductDetailOptionResponse response) {
        return new ProductDetailOptionPrice(
            response.getId(),
            response.getPrice()
        );
    }
}
