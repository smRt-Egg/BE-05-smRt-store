package com.programmers.smrtstore.domain.product.presentation.dto.req;

import com.programmers.smrtstore.domain.product.application.dto.req.ProductRequest;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import org.hibernate.validator.constraints.URL;

@Getter
public class UpdateProductAPIRequest {

    private String name;
    @Min(value = 0, message = "가격은 0 이상이 되어야 합니다.")
    private Integer price;
    @Min(value = 0, message = "수량은 0 이상이 되어야 합니다.")
    private Integer stockQuantity;
    private Integer categoryId;
    @URL
    private String thumbnailUrl;
    @URL
    private String contentImageUrl;
    private String optionNameType1;
    private String optionNameType2;
    private String optionNameType3;

    public ProductRequest toEntity(Long productId) {
        return ProductRequest.builder()
            .id(productId)
            .name(name)
            .price(price)
            .stockQuantity(stockQuantity)
            .category(categoryId == null ? null : Category.findById(categoryId))
            .thumbnail(thumbnailUrl)
            .contentImage(contentImageUrl)
            .optionNameType1(optionNameType1)
            .optionNameType2(optionNameType2)
            .optionNameType3(optionNameType3)
            .build();
    }
}
