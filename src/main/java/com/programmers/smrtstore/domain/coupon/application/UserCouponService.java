package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponQueryRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableUserJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.facade.CouponQuantityFacade;
import com.programmers.smrtstore.domain.coupon.presentation.req.OrderCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.req.SaveCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.req.UpdateUserCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.res.*;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJPARepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponService {

    private final CouponQueryRepository couponQueryRepository;
    private final CouponAvailableUserJpaRepository couponAvailableUserJpaRepository;
    private final UserRepository userRepository;
    private final CouponJpaRepository couponJpaRepository;
    private final ProductJPARepository productJPARepository;
    private final CouponQuantityFacade facade;

    public SaveCouponResponse save(SaveCouponRequest request, Long userId) {
        Long couponId = request.getCouponId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "false"));

        Coupon coupon = couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND, "false"));

        //유저가 쿠폰을 발급받은적 있는지
        Optional<CouponAvailableUser> couponAvailableUser = couponAvailableUserJpaRepository.findByCouponIdAndUserId(couponId, userId);

        return couponAvailableUser
                .map(cu -> {
                    cu.reIssueCoupon();
                    facade.decrease(couponId);
                    return SaveCouponResponse.toDto(cu);
                })
                .orElseGet(() -> {
                    CouponAvailableUser savedCouponAvailableUser = couponAvailableUserJpaRepository.save(CouponAvailableUser.of(coupon, user));
                    facade.decrease(couponId);
                    return SaveCouponResponse.toDto(savedCouponAvailableUser);
                });
    }

    @Transactional(readOnly = true)
    public List<UserCouponResponse> getCouponsByUserId(Long userId) {   //쿠폰 리스트 (마이페이지)

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "false"));

        return couponQueryRepository.findUserCoupons(userId).stream()
                .map(coupon -> UserCouponResponse.toDto(coupon))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserCouponResponse getCouponByUserIdAndCouponId(Long userId, Long couponId) {   //쿠폰 단일 (마이페이지)

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "false"));

        Coupon coupon = couponJpaRepository.findById(couponId)
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND, "false"));

        return couponQueryRepository.findCouponByUserIdAndCouponId(userId, couponId)
                .map(c -> UserCouponResponse.toDto(c))
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_AVAILABLE_USER, "false"));

    }

    @Transactional(readOnly = true)
    public Long getUserCouponQuantity(Long userId) { //유저의쿠폰 잔여개수

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "false"));

        return couponQueryRepository.findUserCouponCount(userId);
    }

}
