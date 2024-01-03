package com.programmers.smrtstore.domain.product.application.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
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
    private Integer price;
    private Integer stockQuantity;
    private Category category;
    private URL thumbnail;
    private URL contentImage;
    private boolean combinationYn;

    public Product toEntity() {
        return Product.builder()
            .name(name)
            .price(price)
            .stockQuantity(stockQuantity)
            .category(category)
            .thumbnail(thumbnail)
            .contentImage(contentImage)
            .combinationYn(combinationYn)
            .build();
    }
}
