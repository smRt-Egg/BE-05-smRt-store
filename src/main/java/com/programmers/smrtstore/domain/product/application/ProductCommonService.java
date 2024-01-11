package com.programmers.smrtstore.domain.product.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ProductCommonService {

    private final ProductJpaRepository productRepository;

    @Transactional(readOnly = true)
    public Product getProduct(Long productId)
        throws ProductException {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
    }

    public void optionValidate(boolean optionYn) throws ProductException {
        if (!optionYn) {
            throw new ProductException(ErrorCode.PRODUCT_NOT_USE_COMBINATION_OPTION);
        }
    }
}
