package com.programmers.smrtstore.domain.product.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductAdditionalOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductAdditionalOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductAdditionalOptionResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductAdditionalOption;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductAdditionalOptionJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductAdditionalService {

    private final ProductAdditionalOptionJpaRepository additionalOptionRepository;
    private final ProductCommonService commonService;

    public ProductAdditionalOptionResponse addAdditionalOption(Long productId,
        CreateProductAdditionalOptionRequest request) {
        Product product = commonService.getProduct(productId);
        ProductAdditionalOption additionalOption = additionalOptionRepository.save(
            request.toEntity(product));
        return ProductAdditionalOptionResponse.from(additionalOption);
    }

    public Long removeAdditionalOption(Long productId, Long additionalId) {
        Product product = commonService.getProduct(productId);
        product.removeAdditionalOption(additionalId);
        return additionalId;
    }

    @Transactional(readOnly = true)
    public List<ProductAdditionalOptionResponse> getAllAdditionalOptions(Long productId) {
        Product product = commonService.getProduct(productId);
        return product.getProductAdditionalOptions().stream()
            .map(ProductAdditionalOptionResponse::from).toList();
    }

    public ProductAdditionalOptionResponse updateAdditionalOption(
        ProductAdditionalOptionRequest request) {
        var additionalOption = additionalOptionRepository.findById(request.getOptionId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        validateOption(request.getProductId(), additionalOption);
        additionalOption.updateGroupName(request.getGroupName());
        additionalOption.updateName(request.getName());
        additionalOption.updatePrice(request.getPrice());
        additionalOption.updateStockQuantity(request.getQuantity());
        return ProductAdditionalOptionResponse.from(additionalOption);
    }

    private void validateOption(Long productId, ProductAdditionalOption option) {
        if (!option.getProduct().getId().equals(productId)) {
            throw new ProductException(ErrorCode.PRODUCT_OPTION_MISMATCH);
        }
    }

}
