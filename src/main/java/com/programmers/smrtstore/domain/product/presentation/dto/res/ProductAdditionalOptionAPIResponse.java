package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductAdditionalOptionResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductAdditionalOptionAPIResponse {

    private Long id;
    private String groupName;
    private String name;
    private String optionType;
    private Integer price;
    private Integer stockQuantity;
    private String productStatusType;

    public static ProductAdditionalOptionAPIResponse from(
        ProductAdditionalOptionResponse response) {
        return new ProductAdditionalOptionAPIResponse(
            response.getId(),
            response.getGroupName(),
            response.getName(),
            response.getOptionType().name(),
            response.getPrice(),
            response.getStockQuantity(),
            response.getProductStatusType().name()
        );
    }
}
