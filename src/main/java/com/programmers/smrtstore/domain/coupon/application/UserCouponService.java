package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.presentation.res.UserCouponResponse;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCouponService {
    private final CouponJpaRepository couponJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    public List<UserCouponResponse> getCouponsByUserId(Long userId) {   //쿠폰 리스트 (마이페이지)

        getUser(userId);

        return couponJpaRepository.findUserCoupons(userId).stream()
                .map(coupon -> UserCouponResponse.from(coupon))
                .toList();
    }

    @Transactional(readOnly = true)
    public Long getUserCouponQuantity(Long userId) { //유저의쿠폰 잔여개수

        getUser(userId);

        return couponJpaRepository.findUserCouponCount(userId);
    }

    private User getUser(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return user;
    }


}

