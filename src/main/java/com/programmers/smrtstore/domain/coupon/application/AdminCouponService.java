package com.programmers.smrtstore.domain.coupon.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.*;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponStatus;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableProductJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponAvailableUserJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponCommonTransactionJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.CouponJpaRepository;
import com.programmers.smrtstore.domain.coupon.infrastructure.facade.CouponQuantityFacade;
import com.programmers.smrtstore.domain.coupon.presentation.req.CreateCouponAvailableProductRequest;
import com.programmers.smrtstore.domain.coupon.presentation.req.CreateCouponAvailableUserRequest;
import com.programmers.smrtstore.domain.coupon.presentation.req.CreateCouponRequest;
import com.programmers.smrtstore.domain.coupon.presentation.res.CouponResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminCouponService {
    private final CouponJpaRepository couponJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final CouponAvailableUserJpaRepository couponAvailableUserJpaRepository;
    private final CouponAvailableProductJpaRepository couponAvailableProductJpaRepository;
    private final CouponCommonTransactionJpaRepository couponCommonTransactionJpaRepository;
    private final CouponQuantityFacade couponQuantityFacade;

    //쿠폰 생성
    public Long createCoupon(CreateCouponRequest request) {
        Coupon savedCoupon = couponJpaRepository.save(CreateCouponRequest.toEntity(request));

        return savedCoupon.getId();
    }

    //쿠폰 목록
    @Transactional(readOnly = true)
    public List<CouponResponse> getCoupons() {

        return couponJpaRepository.findAll().stream()
                .map(CouponResponse::from)
                .collect(Collectors.toList());
    }

    //쿠폰 상세 조회
    @Transactional(readOnly = true)
    public CouponResponse getCouponById(Long id) {
        Coupon coupon = getCoupon(id);
        return CouponResponse.from(coupon);
    }

    //상품에 쿠폰 추가
    public Long addCouponToProduct(Long couponId, CreateCouponAvailableProductRequest request) {
        Long productId = request.getProductId();
        Product product = getProduct(productId);

        Coupon coupon = getCoupon(couponId);

        couponAvailableProductJpaRepository.findByCouponIdAndProductId(couponId, productId).ifPresent(
                e -> {throw new CouponException(ErrorCode.COUPON_ALREADY_APPLIED_PRODUCT);}
        );

        return couponAvailableProductJpaRepository.save(
                CouponAvailableProduct.of(coupon, product)).getId();
    }

    //유저에게 쿠폰 지급(ALLOCATE)
    public Long addCouponToUser(Long couponId, CreateCouponAvailableUserRequest request) {
        Long userId = request.getUserId();
        couponAvailableUserJpaRepository.findByCouponIdAndUserId(couponId, userId).ifPresent(
                e ->   {throw new CouponException(ErrorCode.COUPON_EXIST_BY_USER);});
        User user = getUser(userId);

        Coupon coupon = getCoupon(couponId);


        CouponAvailableUser savedCouponAvailableUser = couponAvailableUserJpaRepository.save(CouponAvailableUser.of(coupon, user));

        couponCommonTransactionJpaRepository.save(
                CouponCommonTransaction.of(user, coupon, CouponStatus.ALLOCATED));

        return savedCouponAvailableUser.getId();
    }
    public Long makeAvailableYesOrNo(Long userId,Long id,boolean availableYn) {
        return availableYn ? makeAvailableYesCouponById(userId, id) : makeAvailableNoCouponById(userId, id);
    }
    //쿠폰 무효화
    private Long makeAvailableNoCouponById(Long userId, Long id) {

        Coupon coupon = getCoupon(id);
        coupon.makeAvailableYes(getUser(userId));
        return id;
    }

    //쿠폰 유효화
    private Long makeAvailableYesCouponById(Long userId, Long id) {
        Coupon coupon = getCoupon(id);
        coupon.makeAvailableNo(getUser(userId));
        return id;
    }

    //쿠폰 수량 업데이트
    public Integer updateCouponQuantity(Long couponId, Integer quantity) {

        return couponQuantityFacade.update(couponId, quantity);

    }

    //자정마다 쿠폰 무효화 작업
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정 00:00에 실행
    public void checkExpiredCoupon() {
        try {
            couponJpaRepository.updateExpiredCoupons();
        } catch (Exception e) {
            log.warn("쿠폰 유효성 스케줄링 중 에러 발생");
        }
    }

    //상품에서 쿠폰 제거
    public void removeCouponOfProduct(Long couponId, Long productId) {
        Coupon coupon = getCoupon(couponId);
        Product product = getProduct(productId);

        couponAvailableProductJpaRepository.deleteByCouponIdAndProductId(couponId, productId);
    }

    //유저에게 쿠폰 회수
    public void removeCouponOfUser(Long couponId, Long userId) {
        Coupon coupon = getCoupon(couponId);
        User user = getUser(userId);

        couponAvailableUserJpaRepository.deleteByCouponIdAndUserId(couponId, userId);
        couponCommonTransactionJpaRepository.save(
                CouponCommonTransaction.of(user, coupon, CouponStatus.EXPIRED)
        );
    }

    private User getUser(Long userId) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return user;
    }

    private Coupon getCoupon(Long id) {
        Coupon coupon = couponJpaRepository.findById(id)
                .orElseThrow(() -> new CouponException(ErrorCode.COUPON_NOT_FOUND));
        return coupon;
    }

    private Product getProduct(Long productId) {
        Product product = productJpaRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
        return product;
    }
}

