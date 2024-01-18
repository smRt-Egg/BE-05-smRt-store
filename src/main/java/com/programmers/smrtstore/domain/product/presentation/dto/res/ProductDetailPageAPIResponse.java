package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.coupon.presentation.res.ProductCouponResponse;
import com.programmers.smrtstore.domain.coupon.presentation.vo.CouponBenefitsPolicy;
import com.programmers.smrtstore.domain.point.application.dto.res.ProductEstimatedPointDto;
import com.programmers.smrtstore.domain.point.presentation.vo.PointBenefits;
import com.programmers.smrtstore.domain.product.application.dto.res.ProductResponse;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNameTypes;
import com.programmers.smrtstore.domain.product.presentation.vo.ProductOptionType;
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
    private List<ProductOptionType> options;
    private List<ProductDetailOptionAPIResponse> optionCombinations;
    private String releaseDate;
    private PointBenefits benefitsView;
    private CouponBenefitsPolicy benefitsPolicy;
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
            productResponse.getContentImage(),
            getOptionResponses(productResponse.getOptionNameTypes()),
            productResponse.getDetailOptionResponses().stream()
                .map(ProductDetailOptionAPIResponse::from).toList(),
            productResponse.getReleaseDate() == null ? null
                : productResponse.getReleaseDate().toString(),
            // benefitsView
            PointBenefits.builder()
                .discountSalePrice(productResponse.getSalePrice())
                .managerPurchasePoint(pointResponse.getDefaultPoint())
                .managerPurchaseExtraPoint(pointResponse.getAdditionalPoint())
                .build(),
            // 쿠폰
            getBenefitsPolicy(couponResponse, productResponse),
            //review
            reviewResponse,
            productResponse.getSalePrice()
        );
    }

    private static List<ProductOptionType> getOptionResponses(
        OptionNameTypes optionNameTypes) {
        if (optionNameTypes == null) {
            return List.of();
        }
        return switch (optionNameTypes.getSize()) {
            case 1 -> List.of(ProductOptionType.from(optionNameTypes.getOptionNameType1()));
            case 2 -> List.of(ProductOptionType.from(optionNameTypes.getOptionNameType1()),
                ProductOptionType.from(optionNameTypes.getOptionNameType2()));
            case 3 -> List.of(ProductOptionType.from(optionNameTypes.getOptionNameType1()),
                ProductOptionType.from(optionNameTypes.getOptionNameType2()),
                ProductOptionType.from(optionNameTypes.getOptionNameType3()));
            default -> List.of();
        };
    }

    private static CouponBenefitsPolicy getBenefitsPolicy(ProductCouponResponse couponResponse,
        ProductResponse productResponse) {
        if (couponResponse == null) {
            return null;
        }
        return CouponBenefitsPolicy.of(couponResponse.getIssuableCoupons(),
            couponResponse.getUnIssuableCoupons(), couponResponse.getMaxDiscountCoupons(),
            productResponse.getPrice(), productResponse.getSalePrice());
    }
}
