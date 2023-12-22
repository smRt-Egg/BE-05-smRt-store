package com.programmers.smrtstore.domain.category.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.programmers.smrtstore.domain.category.domain.entity.Category;
import com.programmers.smrtstore.domain.category.exception.CategoryAlreadyExistException;
import com.programmers.smrtstore.domain.category.exception.CategoryNotFoundException;
import com.programmers.smrtstore.domain.category.infrastructure.CategoryJpaRepository;
import com.programmers.smrtstore.domain.category.presentation.res.CategoryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@DisplayName("Category Service Test")
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Testcontainers
class CategoryServiceTest {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private CategoryService categoryService;


    @DisplayName("test addCategory Success")
    @Test
    void testAddCategorySuccess() {
        // Arrange
        String expectedValue = "COMPUTER";
        // Act
        var actualResult = categoryService.addCategory(expectedValue);
        // Assert
        assertThat(actualResult.getId()).isNotZero();
        assertThat(actualResult.getValue()).isEqualTo(expectedValue);
    }

    @DisplayName("test addCategory Failed when category already exist")
    @Test
    void testAddCategoryFail() {
        // Arrange
        String exceptedValue = "COMPUTER";
        categoryJpaRepository.save(Category.builder()
            .value(exceptedValue)
            .build());
        // Act & Assert
        assertThrows(CategoryAlreadyExistException.class,
            () -> categoryService.addCategory(exceptedValue));
    }

    @DisplayName("test getCategoryById success")
    @Test
    void testGetCategoryByIdSuccess() {
        // Arrange
        String exceptedValue = "COMPUTER";
        Category expectedCategory = categoryJpaRepository.save(Category.builder()
            .value(exceptedValue)
            .build());
        Long expectedCategoryId = expectedCategory.getId();
        // Act
        var actualResult = categoryService.getCategoryById(expectedCategoryId);
        // Assert
        assertThat(actualResult.getId()).isEqualTo(expectedCategoryId);
        assertThat(actualResult.getValue()).isEqualTo(expectedCategory.getValue());
    }

    @DisplayName("test getCategoryById fail when category not found")
    @Test
    void testGetCategoryByIdFail() {
        // Arrange
        Long expectedCategoryId = Long.MAX_VALUE;
        // Act & Assert
        assertThrows(CategoryNotFoundException.class,
            () -> categoryService.getCategoryById(expectedCategoryId));
    }

    @DisplayName("test getAllCategories")
    @Test
    void testGetAllCategories() {
        // Arrange
        String exceptedValue = "COMPUTER";
        Category expectedCategory = categoryJpaRepository.save(Category.builder()
            .value(exceptedValue)
            .build());
        var expectedCategoryResponse = CategoryResponse.from(expectedCategory);
        // Act
        var actualResult = categoryService.getAllCategories();
        // Assert
        assertThat(actualResult).hasSize(1).contains(expectedCategoryResponse);
    }


}