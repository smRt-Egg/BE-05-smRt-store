package com.programmers.smrtstore.domain.product.application;

import static com.programmers.smrtstore.domain.product.application.util.ProductServiceUtil.getProduct;

import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductAdditionalOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductAdditionalOptionRequest;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductAdditionalOptionResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductAdditionalOption;
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

    private final ProductJpaRepository productRepository;
    private final ProductAdditionalOptionJpaRepository additionalOptionRepository;

    public ProductAdditionalOptionResponse addAdditionalOption(Long productId,
        CreateProductAdditionalOptionRequest request) {
        Product product = getProduct(productRepository, productId);
        ProductAdditionalOption additionalOption = additionalOptionRepository.save(
            request.toEntity(product));
        return ProductAdditionalOptionResponse.from(additionalOption);
    }

    public Long removeAdditionalOption(Long productId, Long additionalId) {
        Product product = getProduct(productRepository, productId);
        product.removeAdditionalOption(additionalId);
        return additionalId;
    }

    @Transactional(readOnly = true)
    public List<ProductAdditionalOptionResponse> getAllAdditionalOptions(Long productId) {
        Product product = getProduct(productRepository, productId);
        return product.getProductAdditionalOptions().stream()
            .map(ProductAdditionalOptionResponse::from).toList();
    }

    public ProductAdditionalOptionResponse updateAdditionalOption(
        ProductAdditionalOptionRequest request) {
        var additionalOption = additionalOptionRepository.findById(request.getId())
            .orElseThrow();
        additionalOption.updateValues(request.getQuantity(), request.getPrice(),
            request.getGroupName(), request.getName());
        return ProductAdditionalOptionResponse.from(additionalOption);
    }

}
