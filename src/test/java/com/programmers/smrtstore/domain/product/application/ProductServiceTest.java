package com.programmers.smrtstore.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.programmers.smrtstore.core.config.RedisTestConfig;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductRequest;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductDetailOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("Test Product Service")
@Transactional
@Import(RedisTestConfig.class)
class ProductServiceTest {

    private static final String NAME = "test";
    private static final Category CATEGORY = Category.IT;
    private static final Integer SALE_PRICE = 10000;
    private static final Integer STOCK_QUANTITY = 100;
    private static final String THUMBNAIL_STR = "https://localhost";
    private static final String CONTENT_IMAGE_STR = "https://localhost:8080";

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductJpaRepository productJPARepository;
    @Autowired
    private ProductDetailOptionJpaRepository detailOptionJpaRepository;

    @Test
    void testCreateProductWithoutOptions() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        // Act
        var actualResult = productService.createProduct(request);
        // Assert
        assertThat(actualResult.getId()).isNotNull();
        assertThat(actualResult.getName()).isEqualTo(request.getName());
        assertThat(actualResult.getCategory()).isEqualTo(request.getCategory());
        assertThat(actualResult.getPrice()).isEqualTo(request.getPrice());
        assertThat(actualResult.getStockQuantity()).isEqualTo(request.getStockQuantity());
        assertThat(actualResult.getThumbnail()).isEqualTo(request.getThumbnail());
        assertThat(actualResult.getContentImage()).isEqualTo(request.getContentImage());
        assertThat(actualResult.getCreatedAt()).isNotNull();
        assertThat(actualResult.isCombinationYn()).isFalse();
        assertThat(actualResult.getProductStatusType()).isEqualTo(ProductStatusType.NOT_SALE);
        assertThat(detailOptionJpaRepository.findAll()).hasSize(1);
    }

    @Test
    void testGetProductByIdSuccess() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedResult = productService.createProduct(request);
        // Act
        var actualResult = productService.getProductById(expectedResult.getId());
        // Assert
        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
        assertThat(actualResult.getThumbnail()).isEqualTo(expectedResult.getThumbnail());
        assertThat(actualResult.getCategory()).isEqualTo(expectedResult.getCategory());
        assertThat(actualResult.getPrice()).isEqualTo(expectedResult.getPrice());
        assertThat(actualResult.getStockQuantity()).isEqualTo(expectedResult.getStockQuantity());
        assertThat(actualResult.getCreatedAt()).isEqualTo(expectedResult.getCreatedAt());
    }

    @Test
    void testGetProductByIdFail() {
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.getProductById(Long.MAX_VALUE));
    }

    @Test
    void testGetAllProductsSuccess() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        productService.createProduct(request);
        // Act
        var actualResult = productService.getAllProducts();
        // Assert
        assertThat(actualResult).hasSize(1);
    }

    @Test
    void testReleaseProductSuccess() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedProductId = productService.createProduct(request).getId();
        var expectedProduct = productJPARepository.findById(expectedProductId).get();
        // Act
        var actualResult = productService.releaseProduct(expectedProduct.getId());
        // Assert
        assertThat(actualResult).isEqualTo(expectedProduct.getId());
        assertThat(expectedProduct.getProductStatusType()).isEqualTo(ProductStatusType.SALE);
        assertThat(expectedProduct.getReleaseDate()).isNotNull();
    }

    @Test
    void testReleaseProductFail() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedProductId = productService.createProduct(request).getId();
        var expectedProduct = productJPARepository.findById(expectedProductId).get();
        var expectedResult = productService.releaseProduct(expectedProduct.getId());
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.releaseProduct(expectedResult));
    }

    @Test
    void testMakeProductNotAvailableSuccess() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedProductId = productService.createProduct(request).getId();
        var expectedProduct = productJPARepository.findById(expectedProductId).get();
        productService.releaseProduct(expectedProductId);
        // Act
        var actualResult = productService.makeProductNotAvailable(expectedProductId);
        // Assert
        assertThat(actualResult).isEqualTo(expectedProductId);
        assertThat(expectedProduct.getProductStatusType()).isEqualTo(ProductStatusType.NOT_SALE);
    }

    @Test
    void testMakeProductNotAvailableFail() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productService.makeProductNotAvailable(expectedId));
    }

    @Test
    void testMakeProductAvailableSuccess() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        productService.releaseProduct(expectedId);
        var expectedResult = productService.makeProductNotAvailable(expectedId);
        // Act
        var actualResult = productService.makeProductAvailable(expectedResult);
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void testMakeProductAvailableFailWhenProductAlreadyAvailable() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        productService.releaseProduct(expectedId);
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.makeProductAvailable(expectedId));
    }

    @Test
    void testMakeProductAvailableFailWhenProductNotReleased() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.makeProductAvailable(expectedId));
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        // Act & Assert
        productService.deleteProduct(expectedId);
        assertThat(productJPARepository.findById(expectedId)).isEmpty();
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        ProductRequest updateRequest = ProductRequest.builder()
            .id(expectedId)
            .name("update")
            .category(Category.CLOTHES)
            .price(1000)
            .stockQuantity(10)
            .build();
        // Act
        var actualResult = productService.updateProduct(updateRequest);
        // Assert
        assertThat(actualResult)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", expectedId)
            .hasFieldOrPropertyWithValue("name", updateRequest.getName())
            .hasFieldOrPropertyWithValue("category", updateRequest.getCategory())
            .hasFieldOrPropertyWithValue("price", updateRequest.getPrice())
            .hasFieldOrPropertyWithValue("stockQuantity", updateRequest.getStockQuantity());
    }

    @Test
    void testUpdateProductDiscountRatioSuccess() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        var expectedDiscountRatio = 10;
        // Act
        var actualResult = productService.updateProductDiscountRatio(expectedId,
            expectedDiscountRatio);
        // Assert
        assertThat(actualResult.isDiscountYn()).isTrue();
        assertThat(actualResult.getDiscountRatio()).isEqualTo(expectedDiscountRatio);
    }

    @Test
    void testUpdateProductDiscountRatioFail() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        var expectedDiscountRatio = 0;
        // Assert
        assertThrows(ProductException.class,
            () -> productService.updateProductDiscountRatio(expectedId, expectedDiscountRatio));
    }

    @Test
    void testDisableProductDiscountSuccess() {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(THUMBNAIL_STR)
            .contentImage(CONTENT_IMAGE_STR)
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        productService.updateProductDiscountRatio(expectedId,10);
        var expectedDiscountRatio = 0;
        // Act
        var actualResult = productService.updateProductDiscountRatio(expectedId,
            expectedDiscountRatio);
        // Assert
        assertThat(actualResult.isDiscountYn()).isFalse();
        assertThat(actualResult.getDiscountRatio()).isZero();
    }
}