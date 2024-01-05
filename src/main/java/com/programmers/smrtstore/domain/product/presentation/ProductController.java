package com.programmers.smrtstore.domain.product.presentation;

import com.programmers.smrtstore.domain.coupon.application.CouponService;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.product.application.ProductService;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductThumbnailAPIResponse;
import com.programmers.smrtstore.domain.review.application.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final PointService pointService;
    private final CouponService couponService;
    private final ReviewService reviewService;

    @GetMapping("/{productId}")
    public ResponseEntity<Void> findProductById(@PathVariable Long productId) {
        var productResult = productService.getProductById(productId);
        //var temp = couponService.calculateEstimatedAcmPoint();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductThumbnailAPIResponse>> findAllProducts() {
        var result = productService.getAllProducts().stream()
            .map(productData -> {
                var reviewData = reviewService.getReviewsSizeAndAvgScoreByProductId(productData.getId());
                return ProductThumbnailAPIResponse.of(productData, reviewData);
            })
            .toList();
        return ResponseEntity.ok(result);
    }
}
