package com.programmers.smrtstore.domain.product.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailOptionResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
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
public class ProductDetailService {

    private final ProductJpaRepository productRepository;
    private final ProductDetailOptionJpaRepository detailOptionRepository;
    private final ProductCommonService commonService;

    // 상품 추가 다중 옵션
    public ProductResponse createProduct(CreateProductRequest request,
        List<CreateProductDetailOptionRequest> optionRequests) {
        commonService.optionValidate(request.isCombinationYn());
        Product product = request.toEntity();
        List<ProductDetailOption> productOptions = optionRequests.stream()
            .map(optionRequest -> optionRequest.toEntity(product))
            .toList();
        Product result = productRepository.save(product);
        detailOptionRepository.saveAll(productOptions);
        return ProductResponse.from(result);
    }

    public ProductDetailOptionResponse addProductDetailOption(Long productId,
        CreateProductDetailOptionRequest request) {
        Product product = commonService.getProduct(productId);
        var detailOption = detailOptionRepository.save(request.toEntity(product));
        return ProductDetailOptionResponse.from(detailOption);
    }

    public Long removeProductOption(Long productId, Long productOptionId) {
        Product product = commonService.getProduct(productId);
        commonService.optionValidate(product.isCombinationYn());
        product.removeOption(productOptionId);
        return productOptionId;
    }

    public ProductDetailOptionResponse updateProductOption(
        ProductDetailOptionRequest request) {
        ProductDetailOption detailOption = detailOptionRepository.findById(request.getOptionId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        validateOption(request.getProductId(), detailOption);
        detailOption.updateStockQuantity(request.getQuantity());
        detailOption.updatePrice(request.getPrice());
        detailOption.updateOptionNames(request.getOptionName1(), request.getOptionName2(),
            request.getOptionName3());
        return ProductDetailOptionResponse.from(detailOption);
    }

    private void validateOption(Long productId, ProductDetailOption option) {
        if (!option.getProduct().getId().equals(productId)) {
            throw new ProductException(ErrorCode.PRODUCT_OPTION_MISMATCH);
        }
    }

}
