package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.Coupon;
import com.programmers.smrtstore.domain.coupon.domain.entity.CouponAvailableUser;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponQueryRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableUserJpaRepository;
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
    private final ProductJPARepository productJPARepository;
    public SaveCouponResponse save(SaveCouponRequest request, Long userId) {
        Long couponId = request.getCouponId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, "false"));

        Coupon coupon = couponQueryRepository.findCouponByIdWithPessimistic(couponId)
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND, "false"));

        Optional<CouponAvailableUser> couponAvailableUser = couponAvailableUserJpaRepository.findByCouponIdAndUserId(couponId,userId);
        //처음부터 가져올때 User,UserCoupon,Coupon 페치조인하면 아래서 userRepository,couponJpaRepository 여기서 추가쿼리 안나감! -> 불가. user 유무 검증이 필수라서 페치조인x

        if (couponAvailableUser.isPresent()) { //기존 가입 유저는 기존 쿠폰 이력 관리를 해줘야함!
            CouponAvailableUser cu = couponAvailableUser.get();
            cu.reIssueCoupon();

            return SaveCouponResponse.toDto(cu);
        }

        CouponAvailableUser savedCouponAvailableUser = couponAvailableUserJpaRepository.save(CouponAvailableUser.of(coupon, user));

        return SaveCouponResponse.toDto(savedCouponAvailableUser);
    }


}
