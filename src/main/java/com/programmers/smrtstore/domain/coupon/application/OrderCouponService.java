package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.SelectedCouponsRequest;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.*;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderCouponService {
    private static final int first_index = 0;

    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final CouponJpaRepository couponJpaRepository;

    /**
     * 구분 필요한 메서드
     * <p>
     * 1. getOrderSheetCouponInfoWithSelectedCoupons()
     * - OrderDiscountCalculator.getCouponApplyResult() 사용
     * - select 된 쿠폰이 필요하고, 그 기반으로 반환
     * <p>
     * 2. getOrderSheetCouponInfo()
     * - OrderDiscountCalculator.getMaxDiscountCouponApplyResult() 사용
     * - 자동으로 최고 할인케이스를 반환
     */
    //  1. 쿠폰을 선택해서 파라미터(selectedCoupons)로 주는 메서드 -->여기선 selectedCoupons 그대로 반환하고
    public OrderSheetCouponInfo getOrderSheetCouponInfoWithSelectedCoupons(Long userId, List<OrderedProduct> orderedProducts, SelectedCouponsRequest selectedCouponsRequest) {

        ApplicableProductCoupons applicableProductCoupons = getApplicableProductCoupons(userId, orderedProducts);
        ApplicableDeliveryFeeCoupons applicableDeliveryFeeCoupons = getApplicableDeliveryFeeCoupons();
        List<Coupon> cartCoupons = getCartCoupons();
        SelectedCoupons selectedCoupons = getSelectedCoupons(selectedCouponsRequest);
        Map<Long, List<CouponApplyResult>> discountsByOrderedProductId = OrderCouponDiscountCalculator.getCouponApplyResult(orderedProducts, selectedCouponsRequest);
        List<CouponResponse> cartCouponResponses = cartCoupons.stream().map(cartCoupon -> CouponResponse.from(cartCoupon)).toList();

        return new OrderSheetCouponInfo(discountsByOrderedProductId, selectedCoupons, applicableProductCoupons, applicableDeliveryFeeCoupons, cartCouponResponses);
    }

    //2. 쿠폰 선택X, 쿠폰 서비스에서 자체적으로 최적의 쿠폰 조합을 제공해야함. -> 여기선 selectedCoupons 을 최적 알고리즘으로 반환
    public OrderSheetCouponInfo getOrderSheetCouponInfo(Long userId, List<OrderedProduct> orderedProducts) {


        Map<Long, List<Coupon>> orderedProductCouponMap = new HashMap<>(); // OrderProductId에 적용 가능한 coupon List

        for (OrderedProduct orderedProduct : orderedProducts) {
            List<Coupon> productCoupons = getProductCouponsByUserIdAndProductId(userId, orderedProduct.getProduct().getId());
            orderedProductCouponMap.put(orderedProduct.getId(), productCoupons);
        }

        List<Coupon> cartCoupons = getCartCoupons();
        SelectedCouponsWithCouponApplyResult selectedCouponsWithCouponApplyResult = OrderCouponDiscountCalculator.getMaxDiscountCouponApplyResult(orderedProducts, orderedProductCouponMap, cartCoupons);
        List<CouponResponse> cartCouponResponses = cartCoupons.stream().map(cartCoupon -> CouponResponse.from(cartCoupon)).toList();

        ApplicableProductCoupons applicableProductCoupons = getApplicableProductCoupons(userId, orderedProducts);
        ApplicableDeliveryFeeCoupons applicableDeliveryFeeCoupons = getApplicableDeliveryFeeCoupons();
        SelectedCoupons selectedCoupons = selectedCouponsWithCouponApplyResult.getSelectedCoupons();
        Map<Long, List<CouponApplyResult>> discountsByOrderedProductId = selectedCouponsWithCouponApplyResult.getCouponApplyResult();

        return new OrderSheetCouponInfo(discountsByOrderedProductId, selectedCoupons, applicableProductCoupons, applicableDeliveryFeeCoupons, cartCouponResponses);
    }

    private SelectedCoupons getSelectedCoupons(SelectedCouponsRequest selectedCoupons) {
        Map<Long, CouponResponse> selectedProductCouponListsByOrderedProductId = new HashMap<>();
        Map<Long, CouponResponse> selectedProductDuplicateCouponsByOrderedProductId = new HashMap<>();

        for (Long orderedProductId : selectedCoupons.getSelectedProductCouponListsByOrderedProductId().keySet()) {
            Coupon coupon = selectedCoupons.getSelectedProductCouponListsByOrderedProductId().get(orderedProductId);
            if (coupon.isDuplicationYn()) {
                selectedProductDuplicateCouponsByOrderedProductId.put(orderedProductId, CouponResponse.from(coupon));
            } else selectedProductCouponListsByOrderedProductId.put(orderedProductId, CouponResponse.from(coupon));
        }
        CouponResponse cartCoupon = CouponResponse.from(selectedCoupons.getSelectedCartCoupons());

        return new SelectedCoupons(selectedProductCouponListsByOrderedProductId, selectedProductDuplicateCouponsByOrderedProductId, cartCoupon);
    }

    private ApplicableProductCoupons getApplicableProductCoupons(Long userId, List<OrderedProduct> orderedProducts) {

        Map<Long, List<CouponResponse>> productCouponListsByOrderedProductId = new HashMap<>();
        Map<Long, List<CouponResponse>> productDuplicateCouponListsByOrderedProductId = new HashMap<>();

        for (OrderedProduct orderedProduct : orderedProducts) {
            List<Coupon> coupons = getProductCouponsByUserIdAndProductId(userId, orderedProduct.getProduct().getId());
            List<CouponResponse> duplicationYes = new ArrayList<>();
            List<CouponResponse> duplicationNo = new ArrayList<>();
            for (Coupon coupon : coupons) {
                CouponResponse couponResponse = CouponResponse.from(coupon);
                if (couponResponse.isDuplicationYn())
                    duplicationYes.add(couponResponse);
                else duplicationNo.add(couponResponse);
            }
            productCouponListsByOrderedProductId.put(orderedProduct.getId(), duplicationNo);
            productDuplicateCouponListsByOrderedProductId.put(orderedProduct.getId(), duplicationYes);
        }
        return new ApplicableProductCoupons(productCouponListsByOrderedProductId, productDuplicateCouponListsByOrderedProductId);

    }

    //상품에 적용되면서 유저가 보유하고있는 상품 쿠폰 리스트
    private List<Coupon> getProductCouponsByUserIdAndProductId(Long userId, Long productId) {
        return couponJpaRepository.findCouponByUserIdAndProductId(userId, productId);
    }

    private ApplicableDeliveryFeeCoupons getApplicableDeliveryFeeCoupons() {
        List<Coupon> deliveryFeeCoupons = couponJpaRepository.getDeliveryFeeCoupons();
        return new ApplicableDeliveryFeeCoupons(deliveryFeeCoupons.get(first_index).getId(), deliveryFeeCoupons.size());
    }

    private List<Coupon> getCartCoupons() {
        return couponJpaRepository.getCartCoupons();
    }


    private Product getProduct(Long productId) {

        Product product = productJpaRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        return product;
    }

    private User getUser(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return user;
    }
}
