package com.programmers.smrtstore.domain.product.presentation;

import com.programmers.smrtstore.domain.product.application.ProductDetailService;
import com.programmers.smrtstore.domain.product.application.ProductService;
import com.programmers.smrtstore.domain.product.application.dto.req.ProductDetailOptionRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.CreateProductAPIRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.CreateProductDetailOptionAPIRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.ProductDiscountRatioAPIRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.UpdateDetailOptionAPIRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.req.UpdateProductAPIRequest;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductAPIResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductDetailAPIResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductDetailOptionAPIResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductDiscountAPIResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductThumbnailAPIResponse;
import com.programmers.smrtstore.domain.review.application.ReviewService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping
    public ResponseEntity<ProductAPIResponse> createProduct(
        @Valid @RequestBody CreateProductAPIRequest request) {
        var result =
            request.isCombinationYn() ? productDetailService.createProduct(
                request.toProductRequest(),
                request.toDetailOptionRequests())
                : productService.createProduct(request.toProductRequest());
        return ResponseEntity.ok(ProductAPIResponse.from(result));
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{productId}/detailOptions")
    public ResponseEntity<ProductDetailOptionAPIResponse> addNewDetailOption(
        @PathVariable Long productId,
        @Valid @RequestBody CreateProductDetailOptionAPIRequest request) {
        var result = productDetailService.addProductDetailOption(productId, request.toRequest());
        return ResponseEntity.ok(ProductDetailOptionAPIResponse.from(result));
    }
    // ProductDetailPage

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

    @Secured("ROLE_ADMIN")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDetailAPIResponse> productDetailUpdate(
        @PathVariable Long productId,
        @Valid @RequestBody UpdateProductAPIRequest request) {
        var result = productService.updateProduct(request.toEntity(productId));
        return ResponseEntity.ok(ProductDetailAPIResponse.from(result));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/discount/{productId}")
    public ResponseEntity<ProductDiscountAPIResponse> updateProductDiscount(
        @PathVariable Long productId, @Valid ProductDiscountRatioAPIRequest request) {
        var result = productService.updateProductDiscountRatio(productId,
            request.getDiscountRatio());
        return ResponseEntity.ok(ProductDiscountAPIResponse.from(result));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{productId}/detailOptions/{optionId}")
    public ResponseEntity<ProductDetailOptionAPIResponse> productDetailOptionUpdate(
        @PathVariable Long productId, @PathVariable Long optionId,
        @Valid @RequestBody UpdateDetailOptionAPIRequest request) {
        var result = productDetailService.updateProductOption(ProductDetailOptionRequest.builder()
            .productId(productId)
            .optionId(optionId)
            .optionName1(request.getOptionName1())
            .optionName2(request.getOptionName2())
            .optionName3(request.getOptionName3())
            .quantity(request.getQuantity())
            .price(request.getPrice())
            .build());
        return ResponseEntity.ok(ProductDetailOptionAPIResponse.from(result));
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{productId}/detailOptions/{optionId}")
    public ResponseEntity<Long> removeDetailOption(@PathVariable Long productId,
        @PathVariable Long optionId) {
        var result = productDetailService.removeProductOption(productId, optionId);
        return ResponseEntity.ok(result);
    }

}
