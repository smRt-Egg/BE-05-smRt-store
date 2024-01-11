package com.programmers.smrtstore.domain.product.application;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductOrderService {

    private final ProductCommonService commonService;

    public ProductResponse increaseProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = commonService.getProduct(productId);
        product.increaseStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse increaseProductDetailOptionStockQuantity(Long productId, Long detailOptionId,
        Integer quantityValue) {
        Product product = commonService.getProduct(productId);
        commonService.optionValidate(product.isCombinationYn());
        product.increaseDetailStockQuantity(quantityValue, detailOptionId);
        return ProductResponse.from(product);
    }

    public ProductResponse decreaseProductStockQuantity(Long productId, Integer quantityValue) {
        Product product = commonService.getProduct(productId);
        product.decreaseStockQuantity(quantityValue);
        return ProductResponse.from(product);
    }

    public ProductResponse decreaseProductDetailOptionStockQuantity(Long productId,
        Long productOptionId, Integer quantityValue) {
        Product product = commonService.getProduct(productId);
        commonService.optionValidate(product.isCombinationYn());
        product.decreaseDetailStockQuantity(quantityValue, productOptionId);
        return ProductResponse.from(product);
    }

}
