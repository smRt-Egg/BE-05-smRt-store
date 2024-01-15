package com.programmers.smrtstore.domain.coupon.presentation.vo;

import com.programmers.smrtstore.domain.coupon.presentation.res.UserCouponResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CouponBenefitsPolicy {

    private Integer sellerImmediateDiscountAmount;
    private Integer productDiscountAmount;
    private Integer storeDiscountAmount;
    private Integer totalOrderAmount;
    private Integer totalDiscountAmount;
    private Integer totalPayAmount;
    private List<UserCouponResponse> issuableCoupons;
    private List<UserCouponResponse> unIssuableCoupons;

    public static CouponBenefitsPolicy of(List<UserCouponResponse> issuableCoupons,
        List<UserCouponResponse> unIssuableCoupons, DiscountCoupon maxDiscountCoupon,
        Integer price, Integer salePrice) {
        int sellerImmediateDiscountAmount = price - salePrice;
        int productDiscountAmount;
        int storeDiscountAmount;
        if (maxDiscountCoupon == null){
            productDiscountAmount = 0;
            storeDiscountAmount = 0;
        } else{
            productDiscountAmount = maxDiscountCoupon.getProductCouponDiscount();
            storeDiscountAmount = maxDiscountCoupon.getOrderCouponDiscount();
        }

        int totalDiscountAmount =
            sellerImmediateDiscountAmount + productDiscountAmount + storeDiscountAmount;
        return new CouponBenefitsPolicy(
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
