package com.programmers.smrtstore.domain.coupon.presentation.res;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductCouponAPIResponse {

    private Integer sellerImmediateDiscountAmount;
    private Integer productDiscountAmount;
    private Integer storeDiscountAmount;
    private Integer totalOrderAmount;
    private Integer totalDiscountAmount;
    private Integer totalPayAmount;
    private List<UserCouponResponse> issuableCoupons;
    private List<UserCouponResponse> unIssuableCoupons;

    public static ProductCouponAPIResponse of(List<UserCouponResponse> issuableCoupons,
        List<UserCouponResponse> unIssuableCoupons, DiscountCoupon maxDiscountCoupon,
        Integer price, Integer salePrice) {
        int sellerImmediateDiscountAmount = price - salePrice;
        int productDiscountAmount = maxDiscountCoupon.getProductCouponDiscount();
        int storeDiscountAmount = maxDiscountCoupon.getOrderCouponDiscount();
        int totalDiscountAmount =
            sellerImmediateDiscountAmount + productDiscountAmount + storeDiscountAmount;
        return new ProductCouponAPIResponse(
            sellerImmediateDiscountAmount,
            productDiscountAmount,
            storeDiscountAmount,
            price,
            totalDiscountAmount,
            price - totalDiscountAmount,
            issuableCoupons,
            unIssuableCoupons
        );
    }
}
