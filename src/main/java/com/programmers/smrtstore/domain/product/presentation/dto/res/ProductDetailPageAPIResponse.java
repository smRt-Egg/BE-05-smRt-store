package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.coupon.presentation.res.ProductCouponAPIResponse;
import com.programmers.smrtstore.domain.coupon.presentation.res.ProductCouponResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.ProductEstimatedPointDto;
import com.programmers.smrtstore.domain.point.presentation.dto.res.PointBenefitsAPIResponse;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNameTypes;
import com.programmers.smrtstore.domain.review.application.dto.res.ReviewStatisticsResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailPageAPIResponse {

    private Long id;
    private String category;
    private String name;
    private Integer salePrice;
    private Integer stockQuantity;
    private String productStatusType;
    private String productImage;
    private List<ProductOptionTypeResponse> options;
    private List<ProductDetailOptionAPIResponse> optionCombinations;
    private String releaseDate;
    // discountedSalePrice, managerPurchasePoint, managerPurchaseExtraPoint
    private PointBenefitsAPIResponse benefitsView;
    private ProductCouponAPIResponse benefitsPolicy;
    private ReviewStatisticsResponse reviewAmount;
    private Integer discountedSalePrice;

    public static ProductDetailPageAPIResponse of(ProductResponse productResponse,
        ProductEstimatedPointDto pointResponse, ProductCouponResponse couponResponse,
        ReviewStatisticsResponse reviewResponse) {
        return new ProductDetailPageAPIResponse(
            productResponse.getId(),
            productResponse.getCategory().name(),
            productResponse.getName(),
            productResponse.getPrice(),
            productResponse.getStockQuantity(),
            productResponse.getProductStatusType().name(),
            productResponse.getContentImage().toString(),
            getOptionResponses(productResponse.getOptionNameTypes()),
            productResponse.getDetailOptionResponses().stream()
                .map(ProductDetailOptionAPIResponse::from).toList(),
            productResponse.getReleaseDate().toString(),
            // benefitsView
            null,
            // 쿠폰
            getBenefitsPolicy(couponResponse, productResponse),
            //review
            reviewResponse,
            productResponse.getSalePrice()
        );
    }

    private static List<ProductOptionTypeResponse> getOptionResponses(
        OptionNameTypes optionNameTypes) {
        return switch (optionNameTypes.getSize()) {
            case 1 -> List.of(ProductOptionTypeResponse.from(optionNameTypes.getOptionNameType1()));
            case 2 -> List.of(ProductOptionTypeResponse.from(optionNameTypes.getOptionNameType1()),
                ProductOptionTypeResponse.from(optionNameTypes.getOptionNameType2()));
            case 3 -> List.of(ProductOptionTypeResponse.from(optionNameTypes.getOptionNameType1()),
                ProductOptionTypeResponse.from(optionNameTypes.getOptionNameType2()),
                ProductOptionTypeResponse.from(optionNameTypes.getOptionNameType3()));
            default -> List.of();
        };
    }

    private static ProductCouponAPIResponse getBenefitsPolicy(ProductCouponResponse couponResponse,
        ProductResponse productResponse) {
        if (couponResponse == null) {
            return null;
        }
        return ProductCouponAPIResponse.of(couponResponse.getIssuableCoupons(),
            couponResponse.getUnIssuableCoupons(), couponResponse.getMaxDiscountCoupons(),
            productResponse.getPrice(), productResponse.getSalePrice());
    }
}
