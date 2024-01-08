package com.programmers.smrtstore.domain.product.presentation;

import com.programmers.smrtstore.domain.product.application.ProductDetailService;
import com.programmers.smrtstore.domain.product.application.ProductService;
import com.programmers.smrtstore.domain.product.presentation.dto.req.CreateProductAPIRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.ProductDiscountRatioAPIRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductAPIResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductDiscountAPIResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductThumbnailAPIResponse;
import com.programmers.smrtstore.domain.review.application.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ReviewService reviewService;

    private static final Integer DEFAULT_DISCOUNT_RATIO = 0;

    @PostMapping
    public ResponseEntity<ProductAPIResponse> createProduct(
        @Valid @RequestBody CreateProductAPIRequest request) {
        var result =
            request.isCombinationYn() ? productDetailService.createProduct(request.toProductRequest(),
                request.toDetailOptionRequests())
                : productService.createProduct(request.toProductRequest());
        return ResponseEntity.ok(ProductAPIResponse.from(result));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/discount/{productId}")
    public ResponseEntity<ProductDiscountAPIResponse> updateProductDiscount(
        @PathVariable Long productId, @Valid ProductDiscountRatioAPIRequest request) {
        var result =
            request.getDiscountRatio().equals(DEFAULT_DISCOUNT_RATIO)
                ? productService.disableProductDiscount(productId)
                : productService.updateProductDiscountRatio(productId, request.getDiscountRatio());
        return ResponseEntity.ok(ProductDiscountAPIResponse.from(result));
    }


    @GetMapping
    public ResponseEntity<List<ProductThumbnailAPIResponse>> findAllProducts() {
        var result = productService.getAllProducts().stream()
            .map(productData -> {
                var reviewData = reviewService.getReviewsSizeAndAvgScoreByProductId(
                    productData.getId());
                return ProductThumbnailAPIResponse.of(productData, reviewData);
            })
            .toList();
        return ResponseEntity.ok(result);
    }
}
