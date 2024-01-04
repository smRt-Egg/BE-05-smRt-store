package com.programmers.smrtstore.domain.product.application.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductAdditionalOption;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateProductAdditionalOptionRequest {

    private String groupName;
    private String name;
    private Integer price;
    private Integer stockQuantity;

    public ProductAdditionalOption toEntity(Product product) {
        return ProductAdditionalOption.builder()
            .groupName(groupName)
            .name(name)
            .price(price)
            .stockQuantity(stockQuantity)
            .product(product)
            .build();
    }
}
