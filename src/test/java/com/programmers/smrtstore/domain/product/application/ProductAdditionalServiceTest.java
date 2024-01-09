package com.programmers.smrtstore.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductAdditionalOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.domain.entity.ProductAdditionalOption;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductAdditionalOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DisplayName("Test Product Detail Service")
@Testcontainers
@Transactional
class ProductAdditionalServiceTest {

    private static final String NAME = "test";
    private static final Category CATEGORY = Category.IT;
    private static final Integer SALE_PRICE = 10000;
    private static final Integer STOCK_QUANTITY = 100;
    private static final URL THUMBNAIL;
    private static final URL CONTENT_IMAGE;

    static {
        try {
            THUMBNAIL = new URL("https://localhost");
            CONTENT_IMAGE = new URL("https://localhost:8080");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private ProductJpaRepository productRepository;
    @Autowired
    private ProductAdditionalOptionJpaRepository additionalOptionRepository;
    @Autowired
    private ProductAdditionalService productAdditionalService;
    private Long productId;

    @BeforeEach
    void init() {
        productId = productRepository.save(CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL)
            .contentImage(CONTENT_IMAGE)
            .combinationYn(false)
            .build()
            .toEntity()).getId();
    }

    @Test
    void testAddAdditionalOption() {
        // Arrange
        var request = CreateProductAdditionalOptionRequest.builder()
            .groupName("groupName")
            .name("name")
            .price(1000)
            .stockQuantity(100)
            .build();
        // Act
        var actualResult = productAdditionalService.addAdditionalOption(productId, request);
        // Assert
        assertThat(actualResult)
            .isNotNull()
            .hasFieldOrPropertyWithValue("groupName", request.getGroupName())
            .hasFieldOrPropertyWithValue("name", request.getName())
            .hasFieldOrPropertyWithValue("price", request.getPrice())
            .hasFieldOrPropertyWithValue("stockQuantity", request.getStockQuantity());
    }

    @Test
    void testDeleteAdditionalOptionSuccess() {
        // Arrange
        var product = productRepository.findById(productId).orElseThrow();
        var expectedAdditionalOption = additionalOptionRepository.save(
            ProductAdditionalOption.builder()
                .groupName("groupName")
                .name("name")
                .price(1000)
                .stockQuantity(100)
                .product(product)
                .build());
        // Act
        productAdditionalService.removeAdditionalOption(productId,
            expectedAdditionalOption.getId());
        // Assert
        assertThat(product.getProductAdditionalOptions())
            .isEmpty();
    }

    @Test
    void testDeleteAdditionalOptionFail() {
        // Arrange
        var expectedAdditionalOptionId = Long.MAX_VALUE;
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productAdditionalService.removeAdditionalOption(productId,
                expectedAdditionalOptionId));
    }

    @Test
    void testGetAllAdditionalOptions(){
        // Arrange
        var product = productRepository.findById(productId).orElseThrow();
        additionalOptionRepository.save(
            ProductAdditionalOption.builder()
                .groupName("groupName")
                .name("name")
                .price(1000)
                .stockQuantity(100)
                .product(product)
                .build());
        // Act
        var actualResult = productAdditionalService.getAllAdditionalOptions(productId);
        // Assert
        assertThat(actualResult)
            .isNotEmpty()
            .hasSize(1);
    }
}