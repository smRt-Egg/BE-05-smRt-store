package com.programmers.smrtstore.domain.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.programmers.smrtstore.domain.product.domain.entity.Category;
import com.programmers.smrtstore.domain.product.domain.entity.OptionTag;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductOption;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJPARepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductOptionJPARepository;
import com.programmers.smrtstore.domain.product.presentation.dto.req.CreateProductOptionRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.UpdateProductOptionRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.UpdateProductRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductResponse;
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
    private static final OptionTag OPTION_TAG = OptionTag.CHOICE;
    private static final Integer PRICE = 0;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductJPARepository productJPARepository;
    @Autowired
    private ProductOptionJPARepository productOptionJPARepository;

    @Test
    void testCreateProductWithoutOptions() throws Exception {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build();
        // Act
        var actualResult = productService.createProduct(request);
        // Assert
        assertThat(actualResult.getId()).isNotNull();
        assertThat(actualResult.getName()).isEqualTo(request.getName());
        assertThat(actualResult.getCategory()).isEqualTo(request.getCategory());
        assertThat(actualResult.getSalePrice()).isEqualTo(request.getSalePrice());
        assertThat(actualResult.getStockQuantity()).isEqualTo(request.getStockQuantity());
        assertThat(actualResult.getThumbnail()).isEqualTo(request.getThumbnail());
        assertThat(actualResult.getContentImage()).isEqualTo(request.getContentImage());
        assertThat(actualResult.getCreatedAt()).isNotNull();
        assertThat(actualResult.isOptionYn()).isFalse();
        assertThat(actualResult.isAvailableYn()).isFalse();
    }

    @Test
    void testCreateProductWithOption() throws Exception {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(true)
            .build();
        List<CreateProductOptionRequest> optionRequests = List.of(
            CreateProductOptionRequest.builder()
                .optionName(OPTION_NAME + "1")
                .optionTag(OPTION_TAG)
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .build(),
            CreateProductOptionRequest.builder()
                .optionName(OPTION_NAME + 2)
                .optionTag(OPTION_TAG)
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
        assertThat(actualResult.getSalePrice()).isEqualTo(request.getSalePrice());
        assertThat(actualResult.getStockQuantity()).isEqualTo(200);
        assertThat(actualResult.getThumbnail()).isEqualTo(request.getThumbnail());
        assertThat(actualResult.getContentImage()).isEqualTo(request.getContentImage());
        assertThat(actualResult.getCreatedAt()).isNotNull();
        assertThat(actualResult.isOptionYn()).isTrue();
        assertThat(actualResult.isAvailableYn()).isFalse();
        assertThat(actualResult.getProductOptions()).hasSize(optionRequests.size());
    }

    @DisplayName("Test createProduct Fail when optionYn is false but optionRequests apply")
    @Test
    void testCreateProductFail() throws MalformedURLException {
        // Arrange
        CreateProductRequest request = CreateProductRequest.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build();
        List<CreateProductOptionRequest> optionRequests = List.of(
            CreateProductOptionRequest.builder()
                .optionName(OPTION_NAME + "1")
                .optionTag(OPTION_TAG)
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .build(),
            CreateProductOptionRequest.builder()
                .optionName(OPTION_NAME + "2")
                .optionTag(OPTION_TAG)
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
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        ProductResponse expectedResult = ProductResponse.from(expectedProduct);
        // Act
        var actualResult = productService.getProductById(expectedProduct.getId());
        // Assert
        assertThat(actualResult.getId()).isEqualTo(expectedResult.getId());
        assertThat(actualResult.getName()).isEqualTo(expectedResult.getName());
        assertThat(actualResult.getThumbnail()).isEqualTo(expectedResult.getThumbnail());
        assertThat(actualResult.getCategory()).isEqualTo(expectedResult.getCategory());
        assertThat(actualResult.getSalePrice()).isEqualTo(expectedResult.getSalePrice());
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
        List<Product> products = List.of(
            Product.builder()
                .name(NAME)
                .category(CATEGORY)
                .salePrice(SALE_PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .thumbnail(new URL(THUMBNAIL_STR))
                .contentImage(new URL(CONTENT_IMAGE_STR))
                .optionYn(false)
                .build(),
            Product.builder()
                .name(NAME)
                .category(CATEGORY)
                .salePrice(SALE_PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .thumbnail(new URL(THUMBNAIL_STR))
                .contentImage(new URL(CONTENT_IMAGE_STR))
                .optionYn(false)
                .build()
        );
        productJPARepository.saveAll(products);
        // Act
        var actualResult = productService.getAllProducts();
        // Assert
        assertThat(actualResult).hasSize(products.size());
    }

    @Test
    void testReleaseProductSuccess() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        // Act
        var actualResult = productService.releaseProduct(expectedProduct.getId());
        // Assert
        assertThat(actualResult).isEqualTo(expectedProduct.getId());
        assertThat(expectedProduct.isAvailableYn()).isTrue();
        assertThat(expectedProduct.getReleaseDate()).isNotNull();
    }

    @Test
    void testReleaseProductFail() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedResult = productService.releaseProduct(expectedProduct.getId());
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.releaseProduct(expectedResult));
    }

    @Test
    void testMakeProductNotAvailableSuccess() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedResult = productService.releaseProduct(expectedProduct.getId());
        // Act
        var actualResult = productService.makeProductNotAvailable(expectedResult);
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(expectedProduct.isAvailableYn()).isFalse();
    }

    @Test
    void testMakeProductNotAvailableFail() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedId = expectedProduct.getId();
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.makeProductNotAvailable(expectedId));
    }

    @Test
    void testMakeProductAvailableSuccess() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        productService.releaseProduct(expectedProduct.getId());
        var expectedResult = productService.makeProductNotAvailable(expectedProduct.getId());
        // Act
        var actualResult = productService.makeProductAvailable(expectedResult);
        // Assert
        assertThat(actualResult).isEqualTo(expectedResult);
        assertThat(expectedProduct.isAvailableYn()).isTrue();
    }

    @Test
    void testMakeProductAvailableFailWhenProductAlreadyAvailable() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedId = productService.releaseProduct(expectedProduct.getId());
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.makeProductAvailable(expectedId));
    }

    @Test
    void testMakeProductAvailableFailWhenProductNotReleased() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedId = expectedProduct.getId();
        // Act & Assert
        assertThrows(ProductException.class, () -> productService.makeProductAvailable(expectedId));
    }

    @Test
    void testDeleteProduct() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedId = expectedProduct.getId();
        // Act & Assert
        productService.deleteProduct(expectedId);
        assertThat(productJPARepository.findById(expectedId)).isEmpty();
    }

    @Test
    void testAddProductStockQuantity() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedId = expectedProduct.getId();
        Integer expectedAddQuantity = 100;
        // Act
        var actualResult = productService.addProductStockQuantity(expectedId, expectedAddQuantity);
        // Assert
        assertThat(actualResult.getStockQuantity()).isEqualTo(STOCK_QUANTITY + expectedAddQuantity);
    }

    @Test
    void testAddProductStockQuantityWithProductOption() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(true)
            .build());
        ProductOption expectedProductOption = productOptionJPARepository.save(
            ProductOption.builder()
                .optionName(OPTION_NAME)
                .optionTag(OPTION_TAG)
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .product(expectedProduct)
                .build());
        Integer expectedAddQuantity = 100;
        var expectedProductId = expectedProduct.getId();
        var expectedProductOptionId = expectedProductOption.getId();
        // Act
        var actualResult = productService.addProductStockQuantity(expectedProductId,
            expectedProductOptionId, expectedAddQuantity);
        // Assert
        assertThat(actualResult.getStockQuantity()).isEqualTo(STOCK_QUANTITY + expectedAddQuantity);
    }

    @Test
    void testRemoveProductStockQuantity() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedId = expectedProduct.getId();
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
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedId = expectedProduct.getId();
        Integer expectedRemoveQuantity = 120;
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productService.removeProductStockQuantity(expectedId, expectedRemoveQuantity));
    }

    @Test
    void testRemoveProductStockQuantityWithProductOption() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(true)
            .build());
        ProductOption expectedProductOption = productOptionJPARepository.save(
            ProductOption.builder()
                .optionName(OPTION_NAME)
                .optionTag(OPTION_TAG)
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .product(expectedProduct)
                .build());
        Integer expectedRemoveQuantity = 50;
        var expectedProductId = expectedProduct.getId();
        var expectedProductOptionId = expectedProductOption.getId();
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
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(true)
            .build());
        ProductOption expectedProductOption = productOptionJPARepository.save(
            ProductOption.builder()
                .optionName(OPTION_NAME)
                .optionTag(OPTION_TAG)
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .product(expectedProduct)
                .build());
        Integer expectedRemoveQuantity = 200;
        var expectedProductId = expectedProduct.getId();
        var expectedProductOptionId = expectedProductOption.getId();
        // Act & Assert
        assertThrows(ProductException.class,
            () -> productService.removeProductStockQuantity(expectedProductId,
                expectedProductOptionId, expectedRemoveQuantity));
    }

    @Test
    void testRemoveProductOptionSuccess() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(true)
            .build());
        ProductOption expectedProductOption = productOptionJPARepository.save(
            ProductOption.builder()
                .optionName(OPTION_NAME)
                .optionTag(OPTION_TAG)
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .product(expectedProduct)
                .build());
        var expectedProductId = expectedProduct.getId();
        var expectedProductOptionId = expectedProductOption.getId();
        // Act
        var actualResult = productService.removeProductOption(expectedProductId, expectedProductOptionId);
        // Assert
        var actualProductOption = productOptionJPARepository.findById(expectedProductOptionId);
        assertThat(actualResult.getProductOptions()).isEmpty();
        assertThat(actualProductOption).isEmpty();
    }

    @Test
    void testUpdateProduct() throws MalformedURLException {
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .stockQuantity(STOCK_QUANTITY)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(false)
            .build());
        var expectedId = expectedProduct.getId();
        UpdateProductRequest expectedRequest = UpdateProductRequest.builder()
            .id(expectedId)
            .name(NAME + "1")
            .category(CATEGORY)
            .salePrice(SALE_PRICE + 100)
            .stockQuantity(STOCK_QUANTITY + 10)
            .thumbnail(new URL(CONTENT_IMAGE_STR))
            .contentImage(new URL(THUMBNAIL_STR))
            .build();
        // Act
        var actualResult = productService.updateProduct(expectedRequest);
        // Assert
        assertThat(actualResult.getName()).isEqualTo(expectedRequest.getName());
        assertThat(actualResult.getCategory()).isEqualTo(expectedRequest.getCategory());
        assertThat(actualResult.getSalePrice()).isEqualTo(expectedRequest.getSalePrice());
        assertThat(actualResult.getStockQuantity()).isEqualTo(expectedRequest.getStockQuantity());
        assertThat(actualResult.getThumbnail()).isEqualTo(expectedRequest.getThumbnail());
        assertThat(actualResult.getContentImage()).isEqualTo(expectedRequest.getContentImage());
        assertThat(actualResult.getUpdatedAT()).isNotNull();
    }

    @Test
    void testUpdateProductOption() throws MalformedURLException{
        // Arrange
        Product expectedProduct = productJPARepository.save(Product.builder()
            .name(NAME)
            .category(CATEGORY)
            .salePrice(SALE_PRICE)
            .thumbnail(new URL(THUMBNAIL_STR))
            .contentImage(new URL(CONTENT_IMAGE_STR))
            .optionYn(true)
            .build());
        ProductOption expectedProductOption = productOptionJPARepository.save(
            ProductOption.builder()
                .optionName(OPTION_NAME)
                .optionTag(OPTION_TAG)
                .price(PRICE)
                .stockQuantity(STOCK_QUANTITY)
                .product(expectedProduct)
                .build());
        var expectedOptionId = expectedProductOption.getId();
        UpdateProductOptionRequest expectedRequest = UpdateProductOptionRequest.builder()
            .id(expectedOptionId)
            .optionName(OPTION_NAME+"1")
            .optionTag(OPTION_TAG)
            .price(PRICE+100)
            .build();
        var actualResult = productService.updateProductOption(expectedRequest);
        // Assert
        assertThat(actualResult.getOptionName()).isEqualTo(expectedRequest.getOptionName());
        assertThat(actualResult.getOptionTag()).isEqualTo(expectedRequest.getOptionTag());
        assertThat(actualResult.getPrice()).isEqualTo(expectedRequest.getPrice());
    }

}