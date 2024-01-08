package com.programmers.smrtstore.domain.product.application;

import static com.programmers.smrtstore.domain.product.application.util.ProductServiceUtil.getProduct;

import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductRequest;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductThumbnailResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.infrastructure.ProductDetailOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductJpaRepository productRepository;
    private final ProductDetailOptionJpaRepository productDetailOptionJpaRepository;

    // 상품 추가 옵션 X
    public ProductResponse createProduct(CreateProductRequest request) {
        Product product = productRepository.save(request.toEntity());
        productDetailOptionJpaRepository.save(ProductDetailOption.builder()
            .optionType(OptionType.SINGLE)
            .price(0)
            .stockQuantity(request.getStockQuantity())
            .product(product)
            .build());
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long productId) {
        Product product = getProduct(productRepository, productId);
        return ProductResponse.from(product);
    }

    @Transactional(readOnly = true)
    public List<ProductThumbnailResponse> getAllProducts() {
        return productRepository.findAll()
            .stream().map(ProductThumbnailResponse::from)
            .toList();
    }

    public Long releaseProduct(Long productId) {
        Product product = getProduct(productRepository, productId);
        product.releaseProduct();
        return productId;
    }

    public Long makeProductAvailable(Long productId) {
        Product product = getProduct(productRepository, productId);
        product.makeAvailable();
        return productId;
    }

    public Long makeProductNotAvailable(Long productId) {
        Product product = getProduct(productRepository, productId);
        product.makeNotAvailable();
        return productId;
    }

    public void deleteProduct(Long productId) {
        Product product = getProduct(productRepository, productId);
        productRepository.delete(product);
    }

    public ProductResponse updateProduct(ProductRequest request) {
        Product product = getProduct(productRepository, request.getId());
        product.updateName(request.getName());
        product.updatePrice(request.getPrice());
        product.updateStockQuantity(request.getStockQuantity());
        product.updateCategory(request.getCategory());
        product.updateThumbnail(request.getThumbnail());
        product.updateContentImage(request.getContentImage());
        return ProductResponse.from(product);
    }

    public ProductResponse updateProductDiscountRatio(Long productId, Integer discountRatio) {
        Product product = getProduct(productRepository, productId);
        product.updateDiscountRatio(discountRatio);
        return ProductResponse.from(product);
    }

}
