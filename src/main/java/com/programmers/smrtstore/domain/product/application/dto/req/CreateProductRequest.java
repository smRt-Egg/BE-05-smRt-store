package com.programmers.smrtstore.domain.product.application.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateProductRequest {

    private String name;
    private Integer price;
    private Integer stockQuantity;
    private Category category;
    private String thumbnail;
    private String contentImage;
    private boolean combinationYn;
    private String optionNameType1;
    private String optionNameType2;
    private String optionNameType3;

    public Product toEntity() {
        return Product.builder()
            .name(name)
            .price(price)
            .category(category)
            .thumbnail(thumbnail)
            .contentImage(contentImage)
            .combinationYn(combinationYn)
            .optionNameType1(optionNameType1)
            .optionNameType2(optionNameType2)
            .optionNameType3(optionNameType3)
            .build();
    }
}
