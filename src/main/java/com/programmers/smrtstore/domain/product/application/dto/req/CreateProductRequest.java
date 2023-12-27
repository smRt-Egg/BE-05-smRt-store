package com.programmers.smrtstore.domain.product.application.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.Category;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import java.net.URL;
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
    private Integer salePrice;
    private Integer stockQuantity;
    private Category category;
    private URL thumbnail;
    private URL contentImage;
    private boolean optionYn;

    public Product toEntity() {
        return Product.builder()
            .name(name)
            .salePrice(salePrice)
            .stockQuantity(stockQuantity)
            .category(category)
            .thumbnail(thumbnail)
            .contentImage(contentImage)
            .optionYn(optionYn)
            .build();
    }
}
