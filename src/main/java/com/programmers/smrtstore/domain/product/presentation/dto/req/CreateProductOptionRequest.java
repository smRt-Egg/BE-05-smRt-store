package com.programmers.smrtstore.domain.product.presentation.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.OptionTag;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductOption;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateProductOptionRequest {

    private String optionName;
    private OptionTag optionTag;
    private Integer price;
    private Integer stockQuantity;


    public ProductOption toEntity(Product product) {
        return ProductOption.builder()
            .optionName(optionName)
            .optionTag(optionTag)
            .price(price)
            .stockQuantity(stockQuantity)
            .product(product)
            .build();
    }
}
