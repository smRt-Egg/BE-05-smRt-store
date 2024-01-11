package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponCommonTransaction;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponStatus;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableUserJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponCommonTransactionJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.facade.CouponQuantityFacade;
import com.programmers.smrtstore.domain.coupon.presentation.req.SaveCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.res.*;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCouponService {

    private final CouponAvailableUserJpaRepository couponAvailableUserJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final CouponJpaRepository couponJpaRepository;
    private final CouponQuantityFacade couponQuantityFacade;
    private final CouponCommonTransactionJpaRepository couponCommonTransactionJpaRepository;

    public Long download(SaveCouponRequest request, Long userId) {
        Long couponId = request.getCouponId();
        User user = getUser(userId);
        Coupon coupon = getCoupon(couponId);

        Optional<CouponAvailableUser> couponAvailableUser = checkUserHasCoupon(userId, couponId);

        return couponAvailableUser
                .map(cu -> {
                    return reIssueCoupon(coupon,user,cu, couponId);
                })
                .orElseGet(() -> {
                    return firstIssueCoupon(coupon, user, couponId);
                });
    }

    //Todo: product detail 페이지에서 사용될 메서드
    @Transactional(readOnly = true)
    public ProductCouponResponse getCouponByProductIdAndUserId(Long productId, Long userId) {

        User user = getUser(userId);
        Product product = getProduct(productId);

        List<Coupon> coupons = couponJpaRepository.findCouponByProductId(productId);//product에 해당되는 모든 쿠폰

        return getProductCoupon(coupons, user, product);
    }

    private  ProductCouponResponse getProductCoupon(List<Coupon> coupons, User user, Product product) {
        List<UserCouponResponse> issuableCoupons = new ArrayList<>();
        List<UserCouponResponse> unIssuableCoupons = new ArrayList<>();
        List<Coupon> applicableCoupons = new ArrayList<>();

        for (Coupon coupon : coupons) {
            try {
                CouponAvailableUser.validateCouponWithUser(coupon, user);
                issuableCoupons.add(UserCouponResponse.from(coupon));
                //TODO: 여기까지 검증한 것이 product에 걸려있고, user가 사용할 수 있는지 검증. 단 product 단품에 적용되는지는 모름 -> coupon.discountProduct()

                applicableCoupons.add(coupon);

            } catch (CouponException e) {
                unIssuableCoupons.add(UserCouponResponse.from(coupon));
            }
        }
        //TODO: 메모리 관리 뭐가 좋을지?
        ProductDiscountCalculator productDiscountCalculator = new ProductDiscountCalculator();
        List<Coupon> cartCoupons = couponJpaRepository.getCartCoupons();
        DiscountCoupon maxDiscountCoupons = productDiscountCalculator.discount(applicableCoupons,cartCoupons, product);

        return ProductCouponResponse.of(issuableCoupons, unIssuableCoupons, maxDiscountCoupons);
    }

    // TODO: 유저가 쿠폰을 발급받은적 있는지
    private Optional<CouponAvailableUser> checkUserHasCoupon(Long userId, Long couponId) {
        Optional<CouponAvailableUser> couponAvailableUser = couponAvailableUserJpaRepository.findByCouponIdAndUserId(couponId, userId);
        return couponAvailableUser;
    }

    private Long reIssueCoupon(Coupon coupon,User user,CouponAvailableUser cu, Long couponId) {
        cu.reIssueCoupon();
        couponQuantityFacade.decrease(couponId);
        couponCommonTransactionJpaRepository.save(CouponCommonTransaction.of(user, coupon, CouponStatus.RE_DOWNLOAD));
        return coupon.getId();
    }

    private Long firstIssueCoupon(Coupon coupon, User user, Long couponId) {
        CouponAvailableUser savedCouponAvailableUser = couponAvailableUserJpaRepository.save(CouponAvailableUser.of(coupon, user));
        couponQuantityFacade.decrease(couponId);
        couponCommonTransactionJpaRepository.save(CouponCommonTransaction.of(user, coupon, CouponStatus.DOWNLOAD));
        return coupon.getId();
    }

    private Product getProduct(Long productId) {
        Product product = productJpaRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
        return product;
    }

    private Coupon getCoupon(Long couponId) {

        Coupon coupon = couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND));
        return coupon;
    }

    private User getUser(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return user;
    }

}
