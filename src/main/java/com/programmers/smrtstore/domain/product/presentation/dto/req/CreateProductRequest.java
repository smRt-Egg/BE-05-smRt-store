package com.programmers.smrtstore.domain.product.presentation.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.Category;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import java.net.URL;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductRequest {

    private String name;
    private Integer price;
    private Category category;
    private URL thumbnail;
    private URL contentImage;
    private String origin;

    public Product toEntity() {
        return Product.builder()
            .name(name)
            .price(price)
            .category(category)
            .thumbnail(thumbnail)
            .contentImage(contentImage)
            .origin(origin)
            .build();
    }
}
