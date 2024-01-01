package com.programmers.smrtstore.domain.product.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    TEMP(1, "TEMP"),
    ERROR(-1, "ERROR");


    private final Integer id;
    private final String name;


    public static Category findById(Integer id) {
        for (Category category : Category.values()) {
            if (category.getId().equals(id)) {
                return category;
            }
        }
        return ERROR;
    }

}
