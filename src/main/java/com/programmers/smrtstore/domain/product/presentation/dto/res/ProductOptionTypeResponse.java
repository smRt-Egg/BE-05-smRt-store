package com.programmers.smrtstore.domain.product.presentation.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductOptionTypeResponse {

    private String groupName;

    public static ProductOptionTypeResponse from(String groupName) {
        return new ProductOptionTypeResponse(groupName);
    }
}
