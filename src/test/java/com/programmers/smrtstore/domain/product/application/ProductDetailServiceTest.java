package com.programmers.smrtstore.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductDetailOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DisplayName("Test Product Detail Option Service")
@Testcontainers
@Transactional
@ActiveProfiles("test")
class ProductDetailServiceTest {

    private static final String NAME = "test";
    private static final Category CATEGORY = Category.IT;
    private static final Integer SALE_PRICE = 10000;
    private static final Integer STOCK_QUANTITY = 100;
    private static final URL THUMBNAIL;
    private static final URL CONTENT_IMAGE;
    private static final String OPTION_NAME_TYPE_1 = "optionName1";
    private static final String OPTION_NAME_TYPE_2 = "optionName2";
    private static final String OPTION_NAME_1 = "optionName1";
    private static final String OPTION_NAME_2 = "optionName2";

    static {
        try {
            THUMBNAIL = new URL("https://localhost");
            CONTENT_IMAGE = new URL("https://localhost:8080");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private final CreateProductRequest productRequest = CreateProductRequest.builder()
        .name(NAME)
        .category(CATEGORY)
        .price(SALE_PRICE)
        .stockQuantity(STOCK_QUANTITY)
        .thumbnail(THUMBNAIL)
        .contentImage(CONTENT_IMAGE)
        .combinationYn(true)
        .optionNameType1(OPTION_NAME_TYPE_1)
        .optionNameType2(OPTION_NAME_TYPE_2)
        .build();
    private final List<CreateProductDetailOptionRequest> optionRequests = List.of(
        CreateProductDetailOptionRequest.builder()
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .optionName1(OPTION_NAME_1)
            .optionName2(OPTION_NAME_2)
            .build());
    @Autowired
    private ProductJpaRepository productRepository;
    @Autowired
    private ProductDetailOptionJpaRepository detailOptionRepository;
    @Autowired
    private ProductDetailService productDetailService;
    private Long productId;
    private Long detailOptionId;

    @Test
    void testCreateProductSuccess() {
        // Act
        var actualResult = productDetailService.createProduct(productRequest, optionRequests);
        // Assert
        assertThat(actualResult)
            .isNotNull()
            .hasFieldOrProperty("id").isNotNull()
            .hasFieldOrPropertyWithValue("name", NAME)
            .hasFieldOrProperty("price").isNotNull()
            .hasFieldOrPropertyWithValue("salePrice", SALE_PRICE)
            .hasFieldOrPropertyWithValue("discountRatio", 0)
            .hasFieldOrPropertyWithValue("category", CATEGORY)
            .hasFieldOrPropertyWithValue("stockQuantity", STOCK_QUANTITY)
            .hasFieldOrPropertyWithValue("thumbnail", THUMBNAIL)
            .hasFieldOrPropertyWithValue("contentImage", CONTENT_IMAGE)
            .hasFieldOrProperty("releaseDate").isNotNull()
            .hasFieldOrProperty("createdAt").isNotNull()
            .hasFieldOrPropertyWithValue("productStatusType", ProductStatusType.NOT_SALE)
            .hasFieldOrPropertyWithValue("combinationYn", true)
            .hasFieldOrPropertyWithValue("discountYn", false)
            .hasFieldOrProperty("optionNameTypes").isNotNull()
            .hasFieldOrProperty("detailOptionResponses").isNotNull();
    }

    @Test
    void testCreateProductFail() {
        // Arrange
        CreateProductRequest productRequest = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL)
            .contentImage(CONTENT_IMAGE)
            .combinationYn(false)
            .build();
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productDetailService.createProduct(productRequest, optionRequests));
    }

    @Test
    void addProductDetailOption() {
        // Arrange
        init();
        var optionRequest = CreateProductDetailOptionRequest.builder()
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .optionName1(OPTION_NAME_1)
            .optionName2(OPTION_NAME_2)
            .build();
        // Act
        var actualResult = productDetailService.addProductDetailOption(productId, optionRequest);
        // Assert
        assertThat(actualResult)
            .isNotNull()
            .hasFieldOrProperty("id").isNotNull()
            .hasFieldOrPropertyWithValue("price", SALE_PRICE)
            .hasFieldOrPropertyWithValue("stockQuantity", STOCK_QUANTITY)
            .hasFieldOrProperty("registerDate").isNotNull();
    }

    @Test
    void testRemoveProductOptionSuccess() {
        // Arrange
        init();
        // Act
        productDetailService.removeProductOption(productId, detailOptionId);
        // Assert
        assertThat(productRepository.findById(productId).get().getProductDetailOptions()).isEmpty();
    }

    @Test
    void testRemoveProductOptionFail() {
        // Arrange
        init();
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productDetailService.removeProductOption(productId, 100L));
    }

    @Test
    void testUpdateProductOptionSuccess() {
        // Arrange
        init();
        var request = ProductDetailOptionRequest.builder()
            .productId(productId)
            .optionId(detailOptionId)
            .price(100)
            .quantity(300)
            .optionName1("test")
            .optionName2("test2")
            .build();
        // Act
        var actualResult = productDetailService.updateProductOption(request);
        // Assert
        assertThat(actualResult)
            .isNotNull()
            .hasFieldOrProperty("id").isNotNull()
            .hasFieldOrPropertyWithValue("price", 100)
            .hasFieldOrPropertyWithValue("stockQuantity", 300);
    }

    @Test
    void testUpdateProductOptionFailWhenDuplicatedNameInserted(){
        // Arrange
        init();
        var request = ProductDetailOptionRequest.builder()
            .productId(productId)
            .optionId(detailOptionId)
            .price(100)
            .quantity(300)
            .optionName1("test")
            .optionName2("test")
            .build();
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productDetailService.updateProductOption(request));
    }

    private void init() {
        var product = productRepository.save(productRequest.toEntity());
        detailOptionRepository.saveAll(optionRequests.stream()
            .map(optionRequest -> optionRequest.toEntity(product))
            .toList());
        productId = product.getId();
        detailOptionId = product.getProductDetailOptions().get(0).getId();
    }

}