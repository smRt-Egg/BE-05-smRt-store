package com.programmers.smrtstore.domain.coupon.presentation.res;

import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
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
        List<UserCouponResponse> unIssuableCoupons, List<DiscountCoupon> maxDiscountCoupons,
        Integer price, Integer salePrice) {
        int sellerImmediateDiscountAmount = price - salePrice;
        int productDiscountAmount = 0;
        int storeDiscountAmount = 0;
        for (DiscountCoupon coupon : maxDiscountCoupons) {
            if (coupon.getCoupon().getCouponType().equals(CouponType.PRODUCT)) {
                productDiscountAmount += coupon.getDiscount();
            } else if (coupon.getCoupon().getCouponType().equals(CouponType.CART)){
                storeDiscountAmount += coupon.getDiscount();
            }
        }
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
