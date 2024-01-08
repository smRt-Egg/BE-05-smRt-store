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

    public ProductResponse increaseProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        product.increaseStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse increaseProductDetailOptionStockQuantity(Long productId, Long detailOptionId,
        Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        optionValidate(product.isCombinationYn());
        product.increaseDetailStockQuantity(quantityValue, detailOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse increaseAdditionalOptionStockQuantity(Long productId,
        Long additionalOptionId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        product.increaseAdditionalStockQuantity(quantityValue, additionalOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse decreaseProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        product.decreaseStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse decreaseProductDetailOptionStockQuantity(Long productId,
        Long productOptionId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        optionValidate(product.isCombinationYn());
        product.decreaseDetailStockQuantity(quantityValue, productOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse decreaseAdditionalOptionStockQuantity(Long productId,
        Long additionalOptionId, Integer quantityValue) {
        Product product = getProduct(productRepository, productId);
        product.decreaseAdditionalStockQuantity(quantityValue, additionalOptionId);
        return ProductResponse.from(product);
    }


}
