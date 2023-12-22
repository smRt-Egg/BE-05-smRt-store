package com.programmers.smrtstore.domain.category.application;

import com.programmers.smrtstore.domain.category.domain.entity.Category;
import com.programmers.smrtstore.domain.category.exception.CategoryAlreadyExistException;
import com.programmers.smrtstore.domain.category.exception.CategoryNotFoundException;
import com.programmers.smrtstore.domain.category.infrastructure.CategoryJpaRepository;
import com.programmers.smrtstore.domain.category.presentation.res.CategoryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryJpaRepository categoryJpaRepository;

    public CategoryResponse addCategory(@NonNull String value) {
        categoryJpaRepository.findByValue(value).ifPresent(ignore -> {
            throw new CategoryAlreadyExistException();
        });
        Category category = categoryJpaRepository.save(Category.builder()
            .value(value)
            .build());
        return CategoryResponse.from(category);
    }

    public CategoryResponse getCategoryById(@NonNull Long categoryId) {
        Category category = categoryJpaRepository.findById(categoryId).orElseThrow(
            CategoryNotFoundException::new);
        return CategoryResponse.from(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryJpaRepository.findAll()
            .stream().map(CategoryResponse::from)
            .toList();
    }
}
