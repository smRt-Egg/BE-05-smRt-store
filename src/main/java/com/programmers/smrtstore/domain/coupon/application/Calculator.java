package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.presentation.res.DiscountCoupon;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 알고리즘 구현 후보
 * 우선순위 큐, dfs , 스택?
 */
@Slf4j
public class Calculator {
    private List<DiscountCoupon> maximumDiscountList = new ArrayList<>();
    private PriorityQueue<DiscountCoupon> pq = new PriorityQueue<>();
    private PriorityQueue<DiscountCoupon> duplicationPq = new PriorityQueue<>();

    public List<DiscountCoupon> discount(List<Coupon> coupons, Product product) {

        for (Coupon coupon : coupons) {
            try {
                Long discountValue = coupon.discountProduct(product);
                isDuplication(coupon, discountValue);
            }catch (CouponException e){
                log.info(e.getMessage());
            }
        }

        //TODO: 단품에 대한 최고의 조합은 최대할인률 일반쿠폰 1개랑 중복쿠폰 전부!
        maximumDiscountList.add(pq.poll());

        while (!duplicationPq.isEmpty()) {
            DiscountCoupon poll = duplicationPq.poll();
            maximumDiscountList.add(poll);
        }

        return maximumDiscountList;
    }

    private void isDuplication(Coupon coupon, Long discount) {
        if (coupon.isDuplicationYn()) {
            duplicationPq.add(new DiscountCoupon(coupon, discount));
        } else
            pq.add(new DiscountCoupon(coupon, discount));
    }

}
