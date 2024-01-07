package com.programmers.smrtstore.domain.product.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailOptionResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductThumbnailResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductDetailOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductJpaRepository productJpaRepository;
    private final ProductDetailOptionJpaRepository productDetailOptionJpaRepository;

    // 상품 추가 옵션 X
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = productJpaRepository.save(request.toEntity());
        productDetailOptionJpaRepository.save(ProductDetailOption.builder()
            .optionType(OptionType.SINGLE)
            .price(0)
            .stockQuantity(request.getStockQuantity())
            .product(product)
            .build());
        return ProductResponse.from(product);
    }

    // 상품 추가 다중 옵션
    public ProductResponse createProduct(CreateProductRequest request,
        List<CreateProductDetailOptionRequest> optionRequests) {
        optionValidate(request.isCombinationYn());
        Product product = request.toEntity();
        List<ProductDetailOption> productOptions = optionRequests.stream()
            .map(optionRequest -> optionRequest.toEntity(product))
            .toList();
        Product result = productJpaRepository.save(product);
        productDetailOptionJpaRepository.saveAll(productOptions);
        return ProductResponse.from(result);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = getProduct(productId);
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductThumbnailResponse> getAllProducts() {
        return productJpaRepository.findAll()
            .stream().map(ProductThumbnailResponse::from)
            .toList();
    }

    public Long releaseProduct(Long productId) {
        Product product = getProduct(productId);
        product.releaseProduct();
        return productId;
    }

    public Long makeProductAvailable(Long productId) {
        Product product = getProduct(productId);
        product.makeAvailable();
        return productId;
    }

    public Long makeProductNotAvailable(Long productId) {
        Product product = getProduct(productId);
        product.makeNotAvailable();
        return productId;
    }

    public void deleteProduct(Long productId) {
        Product product = getProduct(productId);
        productJpaRepository.delete(product);
    }

    public ProductResponse addProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = getProduct(productId);
        product.addStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse addProductStockQuantity(Long productId, Long detailOptionId,
        Integer quantityValue) {
        Product product = getProduct(productId);
        optionValidate(product.isCombinationYn());
        product.addStockQuantity(quantityValue, detailOptionId);
        return ProductResponse.from(product);
    }


    public ProductResponse removeProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = getProduct(productId);
        product.removeStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse removeProductStockQuantity(Long productId, Long productOptionId,
        Integer quantityValue) {
        Product product = getProduct(productId);
        optionValidate(product.isCombinationYn());
        product.removeStockQuantity(quantityValue, productOptionId);
        return ProductResponse.from(product);
    }

    public ProductDetailOptionResponse addProductDetailOption(Long productId,
        CreateProductDetailOptionRequest request) {
        Product product = getProduct(productId);
        var detailOption = productDetailOptionJpaRepository.save(request.toEntity(product));
        return ProductDetailOptionResponse.from(detailOption);
    }

    public Long removeProductOption(Long productId, Long productOptionId) {
        Product product = getProduct(productId);
        optionValidate(product.isCombinationYn());
        product.removeOption(productOptionId);
        return productOptionId;
    }

    public ProductResponse updateProduct(ProductRequest request) {
        Product product = getProduct(
            request.getId());
        product.updateValues(request.getName(), request.getSalePrice(), request.getStockQuantity(),
            request.getCategory(), request.getThumbnail(), request.getContentImage());
        return ProductResponse.from(product);
    }

    public ProductDetailOptionResponse updateProductOption(
        ProductDetailOptionRequest request) {
        ProductDetailOption option = productDetailOptionJpaRepository.findById(request.getId())
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        option.updateValues(request.getQuantity(), request.getPrice(), request.getOptionNames());
        return ProductDetailOptionResponse.from(option);
    }

    public ProductResponse updateProductDiscountRatio(Long productId, Integer discountRatio) {
        Product product = getProduct(productId);
        product.updateDiscountRatio(discountRatio);
        return ProductResponse.from(product);
    }

    public ProductResponse disableProductDiscount(Long productId) {
        Product product = getProduct(productId);
        product.disableDiscount();
        return ProductResponse.from(product);
    }

    private Product getProduct(Long productId) {
        return productJpaRepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
    }

    private void optionValidate(boolean optionYn) throws ProductException {
        if (!optionYn) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_USE_OPTION);
        }
    }
}
