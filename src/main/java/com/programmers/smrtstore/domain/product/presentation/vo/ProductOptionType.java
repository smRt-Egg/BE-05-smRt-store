package com.programmers.smrtstore.domain.product.presentation.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductOptionType {

    private String groupName;

    public static ProductOptionType from(String groupName) {
        return new ProductOptionType(groupName);
    }
}
