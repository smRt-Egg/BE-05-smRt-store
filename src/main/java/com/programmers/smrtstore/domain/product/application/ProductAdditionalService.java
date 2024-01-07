package com.programmers.smrtstore.domain.product.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductAdditionalOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductAdditionalOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductAdditionalOptionResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductAdditionalOption;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductAdditionalOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductAdditionalService {

    private final ProductJpaRepository productJpaRepository;
    private final ProductAdditionalOptionJpaRepository productAdditionalOptionJpaRepository;

    public ProductAdditionalOptionResponse addAdditionalOption(Long productId,
        CreateProductAdditionalOptionRequest request) {
        Product product = getProduct(productId);
        ProductAdditionalOption additionalOption = productAdditionalOptionJpaRepository.save(
            request.toEntity(product));
        return ProductAdditionalOptionResponse.from(additionalOption);
    }

    public Long removeAdditionalOption(Long productId, Long additionalId) {
        Product product = getProduct(productId);
        product.removeAdditionalOption(additionalId);
        return additionalId;
    }

    @Transactional(readOnly = true)
    public List<ProductAdditionalOptionResponse> getAllAdditionalOptions(Long productId) {
        Product product = getProduct(productId);
        return product.getProductAdditionalOptions().stream()
            .map(ProductAdditionalOptionResponse::from).toList();
    }

    public ProductAdditionalOptionResponse updateAdditionalOption(
        ProductAdditionalOptionRequest request) {
        var additionalOption = productAdditionalOptionJpaRepository.findById(request.getId())
            .orElseThrow();
        additionalOption.updateValues(request.getQuantity(), request.getPrice(),
            request.getGroupName(), request.getName());
        return ProductAdditionalOptionResponse.from(additionalOption);
    }

    public ProductResponse addAdditionalOptionStockQuantity(Long productId,
        Long additionalOptionId, Integer quantityValue) {
        Product product = getProduct(productId);
        product.addAdditionalStockQuantity(quantityValue, additionalOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse removeAdditionalOptionStockQuantity(Long productId,
        Long additionalOptionId, Integer quantityValue) {
        Product product = getProduct(productId);
        product.removeAdditionalStockQuantity(quantityValue, additionalOptionId);
        return ProductResponse.from(product);
    }

    private Product getProduct(Long productId) {
        return productJpaRepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
    }
}
