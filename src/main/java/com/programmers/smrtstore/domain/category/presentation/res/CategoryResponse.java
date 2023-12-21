package com.programmers.smrtstore.domain.category.presentation.res;

import com.programmers.smrtstore.domain.category.domain.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponse {

    private final Long id;
    private final String value;

    private CategoryResponse(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getValue());
    }
}
