package com.programmers.smrtstore.domain.product.application;

import static com.programmers.smrtstore.domain.product.application.util.ProductServiceUtil.getProduct;
import static com.programmers.smrtstore.domain.product.application.util.ProductServiceUtil.optionValidate;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOrderService {

    private final ProductJpaRepository productRepository;

    public ProductResponse addProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        product.addStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse addProductDetailOptionStockQuantity(Long productId, Long detailOptionId,
        Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        optionValidate(product.isCombinationYn());
        product.addStockQuantity(quantityValue, detailOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse addAdditionalOptionStockQuantity(Long productId,
        Long additionalOptionId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        product.addAdditionalStockQuantity(quantityValue, additionalOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse removeProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        product.removeStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse removeProductDetailOptionStockQuantity(Long productId,
        Long productOptionId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        optionValidate(product.isCombinationYn());
        product.removeStockQuantity(quantityValue, productOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse removeAdditionalOptionStockQuantity(Long productId,
        Long additionalOptionId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        product.removeAdditionalStockQuantity(quantityValue, additionalOptionId);
        return ProductResponse.from(product);
    }


}
