package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.presentation.res.DiscountCoupon;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ProductDiscountCalculator {

    public DiscountCoupon discount(List<Coupon> applicableCoupons, List<Coupon> cartCoupons, Product product) {

        PriorityQueue<DiscountCoupon> pq = new PriorityQueue<>();

        if (applicableCoupons.size() != 0 && cartCoupons.size() == 0) {
            for (Coupon cc : cartCoupons) { //장바구니 쿠폰으로 1번

                if (!cc.validateMinPrice(product.getSalePrice())) continue;
                Integer discountAmountCC = cc.discountProduct(product.getSalePrice());
                pq.add(new DiscountCoupon(cc, null, 0, discountAmountCC,0+discountAmountCC));
            }
            return pq.poll();
        }

        if (cartCoupons.size() == 0 && applicableCoupons.size() != 0) {
            for (Coupon pc : applicableCoupons) { //상품 쿠폰으로 1번
                if (!pc.validateMinPrice(product.getPrice())) continue;
                Integer discountAmountPC = pc.discountProduct(product.getSalePrice());
                pq.add(new DiscountCoupon(pc, null, discountAmountPC, 0,0+discountAmountPC));
            }
            return pq.poll();
        }

        for (Coupon pc : applicableCoupons) { //상품 쿠폰으로 할인
            if (!pc.validateMinPrice(product.getPrice())) continue;
            Integer discountAmountPC = pc.discountProduct(product.getSalePrice());
            for (Coupon cc : cartCoupons) { //장바구니 쿠폰으로 1번
                if (!cc.validateMinPrice(product.getSalePrice()-discountAmountPC)) {
                    pq.add(new DiscountCoupon(pc, null, discountAmountPC, null,discountAmountPC));
                    continue;
                }
                Integer discountAmountCC = cc.discountProduct(discountAmountPC);
                pq.add(new DiscountCoupon(pc, cc, discountAmountPC, discountAmountCC,discountAmountPC+discountAmountCC));

            }
        }
        DiscountCoupon poll = pq.poll();
        return poll;
    }
}
