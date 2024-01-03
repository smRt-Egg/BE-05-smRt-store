package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableUserJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.facade.CouponQuantityFacade;
import com.programmers.smrtstore.domain.coupon.presentation.req.SaveCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.res.*;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponAvailableUserJpaRepository couponAvailableUserJpaRepository;
    private final UserRepository userRepository;
    private final ProductJpaRepository productJpaRepository;
    private final CouponJpaRepository couponJpaRepository;
    private final CouponQuantityFacade couponQuantityFacade;

    public Long download(SaveCouponRequest request, Long userId) {
        Long couponId = request.getCouponId();
        User user = getUser(userId);

        Coupon coupon = couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND));

        Optional<CouponAvailableUser> couponAvailableUser = getCouponAvailableUser(userId, couponId);

        return couponAvailableUser
                .map(cu -> {
                    cu.reIssueCoupon();
                    couponQuantityFacade.decrease(couponId);
                    return cu.getId();
                })
                .orElseGet(() -> {
                    CouponAvailableUser savedCouponAvailableUser = couponAvailableUserJpaRepository.save(CouponAvailableUser.of(coupon, user));
                    couponQuantityFacade.decrease(couponId);
                    return savedCouponAvailableUser.getId();
                });
    }

    @Transactional(readOnly = true)
    public List<UserCouponResponse> getCouponsByUserId(Long userId) {   //쿠폰 리스트 (마이페이지)

        getUser(userId);

        return couponJpaRepository.findUserCoupons(userId).stream()
                .map(coupon -> UserCouponResponse.from(coupon))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getUserCouponQuantity(Long userId) { //유저의쿠폰 잔여개수

        getUser(userId);

        return couponJpaRepository.findUserCouponCount(userId);
    }

    //Todo: product detail 페이지에서 사용될 메서드
    @Transactional(readOnly = true)
    public ProductCouponResponse getCouponByProductIdAndUserId(Long productId, Long userId) {

        User user = getUser(userId);
        Product product = productJpaRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        List<Coupon> coupons = couponJpaRepository.findCouponByProductId(productId);//product에 해당되는 모든 쿠폰

        List<UserCouponResponse> issuableCoupons = new ArrayList<>();
        List<UserCouponResponse> unIssuableCoupons = new ArrayList<>();

        List<Coupon> discountCoupons = new ArrayList<>();

        for (Coupon coupon : coupons) {
            try {
                CouponAvailableUser.validateCouponWithUser(coupon, user);
                issuableCoupons.add(UserCouponResponse.from(coupon));
                //TODO: 여기까지 검증한 것이 product에 걸려있고, user가 사용할 수 있는지 검증. 단 product 단품에 적용되는지는 모름 -> coupon.discountProduct()

                discountCoupons.add(coupon);

            } catch (CouponException e) {
                unIssuableCoupons.add(UserCouponResponse.from(coupon));
            }
        }
        //TODO: 메모리 관리 뭐가 좋을지?
        ProductDiscountCalculator productDiscountCalculator = new ProductDiscountCalculator();
        List<DiscountCoupon> maxDiscountCoupons = productDiscountCalculator.discount(discountCoupons, product.getSalePrice());

        return ProductCouponResponse.of(issuableCoupons, unIssuableCoupons, maxDiscountCoupons);
    }

    private User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "false"));
        return user;
    }

    // TODO: 유저가 쿠폰을 발급받은적 있는지
    private Optional<CouponAvailableUser> getCouponAvailableUser(Long userId, Long couponId) {
        Optional<CouponAvailableUser> couponAvailableUser = couponAvailableUserJpaRepository.findByCouponIdAndUserId(couponId, userId);
        return couponAvailableUser;
    }

}
