package com.programmers.smrtstore.domain.product.application.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.ProductAdditionalOption;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductAdditionalOptionResponse {

    private Long id;
    private String groupName;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private ProductStatusType productStatusType;


    public static ProductAdditionalOptionResponse from(ProductAdditionalOption additionalOption) {
        return new ProductAdditionalOptionResponse(
            additionalOption.getId(),
            additionalOption.getGroupName(),
            additionalOption.getName(),
            additionalOption.getPrice(),
            additionalOption.getStockQuantity(),
            additionalOption.getProductStatusType()
        );
    }
}
