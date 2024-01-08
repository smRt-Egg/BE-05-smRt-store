package com.programmers.smrtstore.domain.product.application.util;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductServiceUtil {

    public static Product getProduct(ProductJpaRepository productRepository, Long productId)
        throws ProductException {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
    }

    public static void optionValidate(boolean optionYn) throws ProductException {
        if (!optionYn) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_USE_OPTION);
        }
    }
}
