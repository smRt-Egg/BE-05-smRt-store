package com.programmers.smrtstore.domain.product.presentation.facade;

import com.programmers.smrtstore.domain.coupon.application.CouponService;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.product.application.ProductService;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductDetailPageAPIResponse;
import com.programmers.smrtstore.domain.product.presentation.dto.res.ProductThumbnailAPIResponse;
import com.programmers.smrtstore.domain.review.application.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductServiceFacadeImpl implements ProductServiceFacade {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final CouponService couponService;
    private final PointService pointService;

    public List<ProductThumbnailAPIResponse> findAllProductThumbnail() {
        return productService.getAllProducts().stream()
            .map(productData -> {
                var reviewData = reviewService.getReviewStatisticsFromProduct(
                    productData.getId());
                return ProductThumbnailAPIResponse.of(productData, reviewData);
            })
            .toList();
    }

    public ProductDetailPageAPIResponse findProductById(Long productId, Long userId) {
        var productDetail = productService.getProductById(productId);
        var couponDetail =
            userId == null ? null : couponService.getCouponByProductIdAndUserId(productId, userId);
        var pointDetail =
            userId == null ? null : pointService.calculateEstimatedAcmPoint(productId, null);
        var reviewDetail = reviewService.getReviewStatisticsFromProduct(productId);
        return ProductDetailPageAPIResponse.of(productDetail, pointDetail, couponDetail,
            reviewDetail);
    }
}
