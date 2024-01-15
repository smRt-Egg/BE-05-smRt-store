package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.presentation.vo.ProductDetailOptionPrice;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDiscountAPIResponse {

    private Long id;
    private Integer price;
    private Integer salePrice;
    private Integer discountRatio;
    private boolean discountYn;

    private List<ProductDetailOptionPrice> detailOptionPriceAPIResponses;

    public static ProductDiscountAPIResponse from(ProductResponse response) {
        return new ProductDiscountAPIResponse(
            response.getId(),
            response.getPrice(),
            response.getSalePrice(),
            response.getDiscountRatio(),
            response.isDiscountYn(),
            response.getDetailOptionResponses().stream().map(ProductDetailOptionPrice::from)
                .toList()
        );
    }
}
