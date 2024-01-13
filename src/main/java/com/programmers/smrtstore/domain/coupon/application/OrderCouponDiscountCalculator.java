package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import com.programmers.smrtstore.domain.coupon.presentation.vo.CartDiscountCoupon;
import com.programmers.smrtstore.domain.coupon.presentation.vo.OrderDiscountCoupon;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.SelectedCouponsRequest;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.CouponApplyResult;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.SelectedCoupons;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.SelectedCouponsWithCouponApplyResult;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;

import java.util.*;

public class OrderCouponDiscountCalculator {

    //TODO: 메서드 1. 자체적으로 최고 할인 케이스를 반환
    public static SelectedCouponsWithCouponApplyResult getMaxDiscountCouponApplyResult(
            List<OrderedProduct> orderedProducts, Map<Long, List<Coupon>> orderedProductCouponMap, List<Coupon> cartCoupons) {

        PriorityQueue<OrderDiscountCoupon> pq = new PriorityQueue<>();

        //상품 즉시할인까지 마친 상품 금액 총계 (즉시할인가+옵션)*수량
        int orderedProductSalePriceSum = orderedProducts.stream()
                .mapToInt(OrderedProduct::getProductSalePriceWithQuantity)
                .sum();

        Map<Long, Boolean> usedCoupon = new HashMap<>(); //해당 쿠폰은 사용되었다.
        Map<Long, Boolean> usedOrderedProduct = new HashMap<>(); //해당 OrderedProduct는 최적의 쿠폰이 적용되어있다.

        //TODO: 큐에 OrderProduct 담아서 상품 쿠폰 우선순위 세우기
        for (OrderedProduct orderedProduct : orderedProducts) {
            usedOrderedProduct.put(orderedProduct.getId(), false);
            for (Coupon coupon : orderedProductCouponMap.get(orderedProduct.getId())) {
                usedCoupon.put(coupon.getId(), false);
                if (!coupon.validateMinPrice(orderedProduct.getTotalPrice())) // 정가 기준(수량,옵션추가한 금액)으로 최소주문금액 체크
                    continue;

                Integer baseAmount = orderedProduct.getProductSalePriceWithQuantity();
                Integer saleAmount = coupon.discountProduct(baseAmount);

                pq.add(new OrderDiscountCoupon(coupon, orderedProduct.getId(), baseAmount, saleAmount));

            }
        }


        Map<Long, List<CouponApplyResult>> discountsByOrderedProductId = new HashMap<>();
        
        int productDiscountSum = 0; //상품의 쿠폰할인 총합 -> 장바구니 쿠폰에서 쓰임
        Map<Long, CouponResponse> selectedProductCouponListsByOrderedProductId = new HashMap<>();
        Map<Long, CouponResponse> selectedProductDuplicateCouponsByOrderedProductId = new HashMap<>();

        
        //TODO: 큐에서 하나씩 빼면서 상품쿠폰 최대 할인을 계산함
        while (!pq.isEmpty()) {
            OrderDiscountCoupon poll = pq.poll();
            if (usedCoupon.get(poll.getCoupon().getId()) || usedOrderedProduct.get(poll.getOrderedProductId())) {
                //다른상품에 사용된 쿠폰이거나 , 이미 쿠폰이 적용된 상품이라면 (이전에 뽑힌 것이 할인값이 더 높을수밖에 없기 때문에)
                continue;
            }
            usedOrderedProduct.put(poll.getOrderedProductId(), true); //상품에 쿠폰 적용처리
            usedCoupon.put(poll.getCoupon().getId(), true); //쿠폰 사용 처리

            //TODO: 상품 쿠폰의 사용 로그 남기기 (CouponApplyResult)
            productDiscountSum += poll.getDiscountAmount();
            discountsByOrderedProductId.put(
                    poll.getOrderedProductId(),
                    List.of(
                            new CouponApplyResult(
                                    poll.getCoupon().getId(),
                                    poll.getCoupon().getCouponType(),
                                    poll.getBaseAmount(),
                                    poll.getDiscountAmount()
                            )
                    )
            );
            if(poll.getCoupon().isDuplicationYn())
                selectedProductDuplicateCouponsByOrderedProductId.put(poll.getOrderedProductId(), CouponResponse.from(poll.getCoupon()));
            else selectedProductCouponListsByOrderedProductId.put(poll.getOrderedProductId(), CouponResponse.from(poll.getCoupon()));
            
        }

        //장바구니 쿠폰에서 사용될 즉시할인과 상품할인이 끝난 금액
        int productCouponAppliedSum = orderedProductSalePriceSum - productDiscountSum;

        //TODO:최대할인 장바구니 쿠폰 구하기
        PriorityQueue<CartDiscountCoupon> cartPq = new PriorityQueue<>();
        for (Coupon coupon : cartCoupons) {
            cartPq.add(new CartDiscountCoupon(coupon, coupon.discountProduct(productCouponAppliedSum)));
        }
        CartDiscountCoupon poll = cartPq.poll(); //->최고 효율의 장바구니 쿠폰
        CouponResponse selectedCartCoupon = CouponResponse.from(poll.getCoupon());

        int cartCouponAppliedSum = productCouponAppliedSum - poll.getDiscountValue();

        //TODO: 장바구니 쿠폰 각 OrderedProduct에 CouponApplyResult를 추가하기
        for (OrderedProduct orderedProduct : orderedProducts) {
            List<CouponApplyResult> couponApplyResults = discountsByOrderedProductId.get(orderedProduct.getId());

            Integer cartCouponAppliedPrice = (orderedProduct.getProductSalePriceWithQuantity() / productCouponAppliedSum) * poll.getDiscountValue();

            if (couponApplyResults.size() == 0) { //상품 쿠폰 없었던 것들
                couponApplyResults.add(new CouponApplyResult(
                        poll.getCoupon().getId(),
                        CouponType.CART,
                        orderedProduct.getProductSalePriceWithQuantity(),
                        cartCouponAppliedPrice));
                continue;
            }
            CouponApplyResult couponApplyResult = couponApplyResults.get(0);
            couponApplyResults.add(new CouponApplyResult(
                    poll.getCoupon().getId(),
                    CouponType.CART,
                    couponApplyResult.getBaseAmount() - couponApplyResult.getDiscountAmount(),
                    cartCouponAppliedPrice)
            );

            discountsByOrderedProductId.put(orderedProduct.getId(), couponApplyResults);
        }
        SelectedCoupons selectedCoupons = new SelectedCoupons(selectedProductCouponListsByOrderedProductId, selectedProductDuplicateCouponsByOrderedProductId, selectedCartCoupon);
        return new SelectedCouponsWithCouponApplyResult(discountsByOrderedProductId,selectedCoupons);
    }


    //TODO: 메서드2. 주어진 select 대로 계산해서 반환
    public static Map<Long, List<CouponApplyResult>> getCouponApplyResult(List<OrderedProduct> orderedProducts, SelectedCouponsRequest selectedCoupons) {

        Map<Long, List<CouponApplyResult>> discountsByOrderedProductId = new HashMap<>();

        //상품 즉시할인까지 마친 상품 금액 총계 (즉시할인가+옵션)*수량
        int orderedProductSalePriceSum = orderedProducts.stream()
                .mapToInt(OrderedProduct::getProductSalePriceWithQuantity)
                .sum();

        int productDiscountSum = 0;

        for (OrderedProduct orderedProduct : orderedProducts) {
            if (selectedCoupons.getSelectedProductCouponListsByOrderedProductId().containsKey(orderedProduct.getId())) { //쿠폰이 적용된 상품이라면
                Coupon coupon = selectedCoupons.getSelectedProductCouponListsByOrderedProductId().get(orderedProduct.getId());

                Integer baseAmount = orderedProduct.getProductSalePriceWithQuantity();
                Integer saleAmount = coupon.discountProduct(baseAmount);

                productDiscountSum += saleAmount;

                discountsByOrderedProductId.put(
                        orderedProduct.getId(),
                        List.of(
                                new CouponApplyResult(
                                        coupon.getId(),
                                        coupon.getCouponType(),
                                        baseAmount,
                                        saleAmount
                                )
                        )
                );

            }
        }

        int productCouponAppliedSum = orderedProductSalePriceSum - productDiscountSum;  //즉시할인가 총합 - 상품쿠폰 적용가 -> 상품쿠폰까지 적용된 총합

        //TODO: 장바구니 쿠폰을 각 OrderedProduct에 CouponApplyResult를 추가하기
        Coupon selectedCartCoupons = selectedCoupons.getSelectedCartCoupons();
        int cartCouponApliedSum = selectedCartCoupons.discountProduct(productCouponAppliedSum);

        for (OrderedProduct orderedProduct : orderedProducts) {
            List<CouponApplyResult> couponApplyResults = discountsByOrderedProductId.get(orderedProduct.getId());

            Integer cartCouponAppliedPrice = (orderedProduct.getProductSalePriceWithQuantity() / productCouponAppliedSum) * cartCouponApliedSum;

            if (couponApplyResults.size() == 0) { //상품 쿠폰 적용 없었던 것들
                discountsByOrderedProductId.put(
                        orderedProduct.getId(),
                        List.of(new CouponApplyResult(
                                selectedCartCoupons.getId(),
                                CouponType.CART,
                                orderedProduct.getProductSalePriceWithQuantity(), //baseAmount = 상품할인 안한 즉시할인(옵션o,수량o) 총합 가격
                                cartCouponAppliedPrice)));

                continue;
            }
            CouponApplyResult couponApplyResult = couponApplyResults.get(0);
            couponApplyResults.add(new CouponApplyResult(
                    orderedProduct.getId(),
                    CouponType.CART,
                    couponApplyResult.getBaseAmount() - couponApplyResult.getDiscountAmount(),
                    cartCouponAppliedPrice)
            );
            discountsByOrderedProductId.put(orderedProduct.getId(), couponApplyResults);

        }
        return discountsByOrderedProductId;
    }

}
