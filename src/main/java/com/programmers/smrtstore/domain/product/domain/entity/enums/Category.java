package com.programmers.smrtstore.domain.product.domain.entity.enums;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    CLOTHES(1), FOOD(2), ELECTRIC(3), HOUSE(4), IT(5);


    private final Integer id;

    public static Category findById(Integer id) {
        for (Category category : Category.values()) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        throw new ProductException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND);
    }

}
