package com.programmers.smrtstore.domain.product.application.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNames;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateProductDetailOptionRequest {

    private String optionName1;
    private String optionName2;
    private String optionName3;
    private Integer price;
    private Integer stockQuantity;


    public ProductDetailOption toEntity(Product product) {
        return ProductDetailOption.builder()
            .optionNames(getOptionNames())
            .optionType(OptionType.COMBINATION)
            .price(price)
            .stockQuantity(stockQuantity)
            .product(product)
            .build();
    }

    private OptionNames getOptionNames() {
        return OptionNames.builder()
            .optionName1(optionName1)
            .optionName2(optionName2)
            .optionName3(optionName3)
            .build();
    }
}
