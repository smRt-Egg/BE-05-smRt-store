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
import com.programmers.smrtstore.domain.product.infrastructure.ProductJPARepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponService {

    private final CouponAvailableUserJpaRepository couponAvailableUserJpaRepository;
    private final UserRepository userRepository;
    private final ProductJPARepository productJPARepository;
    private final CouponJpaRepository couponJpaRepository;
    private final CouponQuantityFacade couponQuantityFacade;

    public Long save(SaveCouponRequest request, Long userId) {
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
