package com.programmers.smrtstore.domain.product.application.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductThumbnailResponse {
    private Long id;
    private String name;
    private Integer price;
    private boolean discountYn;
    private Integer salePrice;
    private Integer discountRatio;
    private String thumbnail;


    public static ProductThumbnailResponse from(Product product) {
        return new ProductThumbnailResponse(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.isDiscountYn(),
            product.getSalePrice(),
            product.getDiscountRatio(),
            product.getThumbnail()
        );
    }
}
