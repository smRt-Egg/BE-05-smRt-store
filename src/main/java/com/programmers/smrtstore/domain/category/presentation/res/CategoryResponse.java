package com.programmers.smrtstore.domain.category.presentation.res;

import com.programmers.smrtstore.domain.category.domain.entity.Category;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CategoryResponse that = (CategoryResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
