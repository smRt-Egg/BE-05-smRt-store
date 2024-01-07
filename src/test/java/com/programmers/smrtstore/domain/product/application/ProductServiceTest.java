package com.programmers.smrtstore.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
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
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DisplayName("Test Product Service")
@Testcontainers
@Transactional
class ProductServiceTest {

    private static final String NAME = "test";
    private static final Category CATEGORY = Category.TEMP;
    private static final Integer SALE_PRICE = 10000;
    private static final Integer STOCK_QUANTITY = 100;
    private static final String THUMBNAIL_STR = "https://localhost";
    private static final String CONTENT_IMAGE_STR = "https://localhost:8080";
    private static final String OPTION_NAME = "option";
    private static final Integer PRICE = 0;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductJpaRepository productJPARepository;
    @Autowired
    private ProductDetailOptionJpaRepository detailOptionJpaRepository;

    @Test
    void testCreateProductWithoutOptions() throws Exception {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
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
    void testCreateProductWithOption() throws Exception {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(true)
            .build();
        List<CreateProductDetailOptionRequest> optionRequests = List.of(
            CreateProductDetailOptionRequest.builder()
                .optionName1(OPTION_NAME + "1")
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .build(),
            CreateProductDetailOptionRequest.builder()
                .optionName1(OPTION_NAME + "2")
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .build()
        );
        // Act
        var actualResult = productService.createProduct(request, optionRequests);
        // Assert
        assertThat(actualResult.getId()).isNotNull();
        assertThat(actualResult.getName()).isEqualTo(request.getName());
        assertThat(actualResult.getCategory()).isEqualTo(request.getCategory());
        assertThat(actualResult.getPrice()).isEqualTo(request.getPrice());
        assertThat(actualResult.getStockQuantity()).isEqualTo(200);
        assertThat(actualResult.getThumbnail()).isEqualTo(request.getThumbnail());
        assertThat(actualResult.getContentImage()).isEqualTo(request.getContentImage());
        assertThat(actualResult.getCreatedAt()).isNotNull();
        assertThat(actualResult.isCombinationYn()).isTrue();
        assertThat(actualResult.getProductStatusType()).isEqualTo(ProductStatusType.NOT_SALE);
        assertThat(actualResult.getDetailOptionResponses()).hasSize(optionRequests.size());
    }

    @DisplayName("Test createProduct Fail when optionYn is false but optionRequests apply")
    @Test
    void testCreateProductFail() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        List<CreateProductDetailOptionRequest> optionRequests = List.of(
            CreateProductDetailOptionRequest.builder()
                .optionName1(OPTION_NAME + "1")
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .build(),
            CreateProductDetailOptionRequest.builder()
                .optionName1(OPTION_NAME + "2")
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .build()
        );
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productService.createProduct(request, optionRequests));
    }

    @Test
    void testGetProductByIdSuccess() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
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
    void testGetAllProductsSuccess() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        productService.createProduct(request);
        // Act
        var actualResult = productService.getAllProducts();
        // Assert
        assertThat(actualResult).hasSize(1);
    }

    @Test
    void testReleaseProductSuccess() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
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
    void testReleaseProductFail() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedProductId = productService.createProduct(request).getId();
        var expectedProduct = productJPARepository.findById(expectedProductId).get();
        var expectedResult = productService.releaseProduct(expectedProduct.getId());
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.releaseProduct(expectedResult));
    }

    @Test
    void testMakeProductNotAvailableSuccess() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
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
    void testMakeProductNotAvailableFail() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productService.makeProductNotAvailable(expectedId));
    }

    @Test
    void testMakeProductAvailableSuccess() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
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
    void testMakeProductAvailableFailWhenProductAlreadyAvailable() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        productService.releaseProduct(expectedId);
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.makeProductAvailable(expectedId));
    }

    @Test
    void testMakeProductAvailableFailWhenProductNotReleased() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.makeProductAvailable(expectedId));
    }

    @Test
    void testDeleteProduct() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        // Act & Assert
        productService.deleteProduct(expectedId);
        assertThat(productJPARepository.findById(expectedId)).isEmpty();
    }

    @Test
    void testAddProductStockQuantity() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        Integer expectedAddQuantity = 100;
        // Act
        var actualResult = productService.addProductStockQuantity(expectedId, expectedAddQuantity);
        // Assert
        assertThat(actualResult.getStockQuantity()).isEqualTo(STOCK_QUANTITY + expectedAddQuantity);
    }


    @Test
    void testRemoveProductStockQuantity() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        Integer expectedRemoveQuantity = 50;
        // Act
        var actualResult = productService.removeProductStockQuantity(expectedId,
            expectedRemoveQuantity);
        // Assert
        assertThat(actualResult.getStockQuantity()).isEqualTo(
            STOCK_QUANTITY - expectedRemoveQuantity);
    }

    @DisplayName("Test removeProductStockQuantity Fail when remove quantity more then stock quantity")
    @Test
    void testRemoveProductStockQuantityFail() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        Integer expectedRemoveQuantity = 120;
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productService.removeProductStockQuantity(expectedId, expectedRemoveQuantity));
    }

    @Test
    void testRemoveProductStockQuantityWithProductOption() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(true)
            .build();
        List<CreateProductDetailOptionRequest> optionRequests = List.of(
            CreateProductDetailOptionRequest.builder()
                .optionName1(OPTION_NAME + "1")
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .build()
        );
        var expectedResponse = productService.createProduct(request, optionRequests);
        var expectedProductId = expectedResponse.getId();
        var expectedProductOptionId = expectedResponse.getDetailOptionResponses().get(0).getId();
        Integer expectedRemoveQuantity = 50;
        // Act
        var actualResult = productService.removeProductStockQuantity(expectedProductId,
            expectedProductOptionId, expectedRemoveQuantity);
        // Assert
        assertThat(actualResult.getStockQuantity()).isEqualTo(
            STOCK_QUANTITY - expectedRemoveQuantity);
    }

    @DisplayName("Test removeProductStockQuantity with option Fail when remove quantity more then stock quantity")
    @Test
    void testRemoveProductStockQuantityWithProductOptionFail() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(true)
            .build();
        List<CreateProductDetailOptionRequest> optionRequests = List.of(
            CreateProductDetailOptionRequest.builder()
                .optionName1(OPTION_NAME + "1")
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .build()
        );
        var expectedResponse = productService.createProduct(request, optionRequests);
        var expectedProductId = expectedResponse.getId();
        var expectedProductOptionId = expectedResponse.getDetailOptionResponses().get(0).getId();
        Integer expectedRemoveQuantity = 200;
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productService.removeProductStockQuantity(expectedProductId,
                expectedProductOptionId, expectedRemoveQuantity));
    }

    @Test
    void testUpdateProductDiscountRatioSuccess() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        var expectedDiscountRatio = 10.5f;
        // Act
        var actualResult = productService.updateProductDiscountRatio(expectedId,
            expectedDiscountRatio);
        // Assert
        assertThat(actualResult.isDiscountYn()).isTrue();
        assertThat(actualResult.getDiscountRatio()).isEqualTo(expectedDiscountRatio);
    }

    @Test
    void testUpdateProductDiscountRatioFail() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        var expectedDiscountRatio = 0f;
        // Assert
        assertThrows(ProductException.class,
            () -> productService.updateProductDiscountRatio(expectedId, expectedDiscountRatio));
    }

    @Test
    void testDisableProductDiscountSuccess() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        var expectedDiscountRatio = 10.5f;
        productService.updateProductDiscountRatio(expectedId,
            expectedDiscountRatio);
        // Act
        var actualResult = productService.disableProductDiscount(expectedId);
        // Assert
        assertThat(actualResult.isDiscountYn()).isFalse();
        assertThat(actualResult.getDiscountRatio()).isZero();
    }

    @Test
    void testDisableProductDiscountFail() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .price(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .combinationYn(false)
            .build();
        var expectedId = productService.createProduct(request).getId();
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productService.disableProductDiscount(expectedId));
    }
}