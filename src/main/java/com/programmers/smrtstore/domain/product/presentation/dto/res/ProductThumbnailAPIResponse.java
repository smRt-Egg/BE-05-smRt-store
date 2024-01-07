package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductThumbnailResponse;
import com.programmers.smrtstore.domain.review.application.dto.res.ReviewDetailResponse;
import java.net.URL;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductThumbnailAPIResponse {

    private Long id;
    private String name;
    private Integer price;
    private boolean discountYn;
    private Integer salePrice;
    private Integer discountRatio;
    private URL thumbnail;
    private Integer reviewCount;
    private Float reviewAvgScore;

    public static ProductThumbnailAPIResponse of(ProductThumbnailResponse productResponse,
        ReviewDetailResponse reviewResponse) {
        return new ProductThumbnailAPIResponse(
            productResponse.getId(),
            productResponse.getName(),
            productResponse.getPrice(),
            productResponse.isDiscountYn(),
            productResponse.getSalePrice(),
            productResponse.getDiscountRatio(),
            productResponse.getThumbnail(),
            reviewResponse.getSize(),
            reviewResponse.getAvgScore()
        );
    }
}
