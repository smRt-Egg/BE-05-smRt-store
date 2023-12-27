package com.programmers.smrtstore.domain.product.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.UpdateProductOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.UpdateProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductOptionResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.UpdateProductResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductOption;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJPARepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductOptionJPARepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductJPARepository productJPARepository;
    private final ProductOptionJPARepository productOptionJPARepository;

    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = productJPARepository.save(request.toEntity());
        return ProductResponse.from(product);
    }

    public ProductResponse createProduct(CreateProductRequest request,
        List<CreateProductOptionRequest> optionRequests) {
        optionValidate(request.isOptionYn());
        Product product = request.toEntity();
        List<ProductOption> productOptions = optionRequests.stream()
            .map(optionRequest -> optionRequest.toEntity(product))
            .toList();
        Product result = productJPARepository.save(product);
        productOptionJPARepository.saveAll(productOptions);
        return ProductResponse.from(result);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        return productJPARepository.findAll()
            .stream().map(ProductResponse::from)
            .toList();
    }

    public Long releaseProduct(Long productId) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        product.releaseProduct();
        return productId;
    }

    public Long makeProductAvailable(Long productId) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        product.makeAvailable();
        return productId;
    }

    public Long makeProductNotAvailable(Long productId) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        product.makeNotAvailable();
        return productId;
    }

    public void deleteProduct(Long productId) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        productJPARepository.delete(product);
    }

    public ProductResponse addProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        product.addStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse addProductStockQuantity(Long productId, Long productOptionId,
        Integer quantityValue) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        optionValidate(product.isOptionYn());
        product.addStockQuantity(quantityValue, productOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse removeProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        product.removeStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse removeProductStockQuantity(Long productId, Long productOptionId,
        Integer quantityValue) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        optionValidate(product.isOptionYn());
        product.removeStockQuantity(quantityValue, productOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse removeProductOption(Long productId, Long productOptionId) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        optionValidate(product.isOptionYn());
        ProductOption productOption = productOptionJPARepository.findById(productOptionId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        product.removeOption(productOption);
        productOptionJPARepository.delete(productOption);
        return ProductResponse.from(product);
    }

    public UpdateProductResponse updateProduct(UpdateProductRequest request) {
        Product product = productJPARepository.findById(request.getId())
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        product.updateValues(request.getName(), request.getSalePrice(), request.getStockQuantity(),
            request.getCategory(), request.getThumbnail(), request.getContentImage());
        return UpdateProductResponse.from(product);
    }

    public ProductOptionResponse updateProductOption(UpdateProductOptionRequest request) {
        ProductOption option = productOptionJPARepository.findById(request.getId())
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        option.updateValues(request.getOptionName(), request.getPrice(), request.getOptionTag());
        return ProductOptionResponse.from(option);
    }

    private void optionValidate(boolean optionYn) throws ProductException {
        if (!optionYn) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_USE_OPTION);
        }
    }
}
