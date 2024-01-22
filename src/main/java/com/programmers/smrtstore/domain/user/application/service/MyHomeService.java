package com.programmers.smrtstore.domain.user.application.service;

import static com.programmers.smrtstore.core.properties.ErrorCode.USERNAME_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;
import static com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus.PURCHASE_CONFIRMED;

import com.programmers.smrtstore.domain.cart.application.CartService;
import com.programmers.smrtstore.domain.coupon.application.UserCouponService;
import com.programmers.smrtstore.domain.keep.application.KeepService;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.FindKeepByCategoryRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import com.programmers.smrtstore.domain.orderManagement.order.application.OrderServiceImpl;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.programmers.smrtstore.domain.review.application.ReviewService;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import com.programmers.smrtstore.domain.user.infrastructure.UserQueryRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.DurationRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyAllKeepsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyCategoryKeepsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyCouponsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyHomeResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyOrdersResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyQnaResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyReviewsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyWritableReviewsResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MyHomeService {

    private final UserQueryRepository userQueryRepository;
    private final UserJpaRepository userJpaRepository;
    private final CartService cartService;
    private final UserCouponService userCouponService;
    private final KeepService keepService;
    private final OrderServiceImpl orderService;
    private final PointService pointService;
    private final ReviewService reviewService;

    @Transactional(readOnly = true)
    public MyHomeResponse getMyHome(Long userId) {
        User user = findByUserId(userId);

        return MyHomeResponse.builder()
            .nickName(user.getNickName())
            .thumbnail(user.getThumbnail())
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .cartCount(cartService.getAllCarts(userId).size())
            .reviewPoint(pointService.calculateMaximumPointForUnwrittenReview(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .allKeeps(keepService.getAllKeepsByUserId(userId, userId))
            .clothesKeeps(getKeepsByCategory(userId, Category.CLOTHES))
            .clothesKeepsCount(getKeepsByCategory(userId, Category.CLOTHES).size())
            .foodKeeps(getKeepsByCategory(userId, Category.FOOD))
            .foodKeepsCount(getKeepsByCategory(userId, Category.FOOD).size())
            .electricKeeps(getKeepsByCategory(userId, Category.ELECTRIC))
            .electricKeepsCount(getKeepsByCategory(userId, Category.ELECTRIC).size())
            .houseKeeps(getKeepsByCategory(userId, Category.HOUSE))
            .houseKeepsCount(getKeepsByCategory(userId, Category.HOUSE).size())
            .itKeeps(getKeepsByCategory(userId, Category.IT))
            .itKeepsCount(getKeepsByCategory(userId, Category.IT).size())
            .build();
    }

    @Transactional(readOnly = true)
    public MyOrdersResponse getOrders(Long userId) {
        User user = findByUserId(userId);
        String username = findUsernameByUserId(userId);
        return MyOrdersResponse.builder()
            .nickName(user.getNickName())
            .username(username)
            .point(user.getPoint())
            .unwrittenReviewPoint(pointService.calculateMaximumPointForUnwrittenReview(userId))
            .reviewCount(reviewService.getReviewsByUserId(userId, userId).size())
            .orderList(orderService.getOrderPreviewsByUserId(userId))
            .build();
    }

    @Transactional(readOnly = true)
    public MyAllKeepsResponse getMyAllKeeps(Long userId) {
        User user = findByUserId(userId);
        String username = findUsernameByUserId(userId);
        return MyAllKeepsResponse.builder()
            .nickName(user.getNickName())
            .username(username)
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            .allKeeps(keepService.getAllKeepsByUserId(userId, userId))
            .build();
    }

    @Transactional(readOnly = true)
    public MyCategoryKeepsResponse getMyKeepsByCategory(Long userId, Integer categoryId) {
        User user = findByUserId(userId);
        String username = findUsernameByUserId(userId);
        return MyCategoryKeepsResponse.builder()
            .nickName(user.getNickName())
            .username(username)
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            .categoryKeeps(getKeepsByCategory(userId, Category.findById(categoryId)))
            .build();
    }

    @Transactional(readOnly = true)
    public MyReviewsResponse getMyReviews(Long userId, DurationRequest request) {
        User user = findByUserId(userId);
        String username = findUsernameByUserId(userId);
        return MyReviewsResponse.builder()
            .nickName(user.getNickName())
            .username(username)
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            //리뷰 리스트
            .build();
    }

    @Transactional(readOnly = true)
    public MyWritableReviewsResponse getMyWritableReviews(Long userId) {
        User user = findByUserId(userId);
        String username = findUsernameByUserId(userId);
        return MyWritableReviewsResponse.builder()
            .nickName(user.getNickName())
            .username(username)
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            //작성 안 한 리뷰 리스트
            .build();
    }

    public MyQnaResponse getMyQna(Long userId, DurationRequest request) {
        User user = findByUserId(userId);
        String username = findUsernameByUserId(userId);
        return MyQnaResponse.builder()
            .nickName(user.getNickName())
            .username(username)
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            //qna 리스트
            .build();
    }

    public MyOrdersResponse getPurchasedConfirmedOrders(Long userId) {
        User user = findByUserId(userId);
        String username = findUsernameByUserId(userId);
        List<OrderStatus> orderStatusList = new ArrayList<>();
        orderStatusList.add(PURCHASE_CONFIRMED);
        return MyOrdersResponse.builder()
            .nickName(user.getNickName())
            .username(username)
            .point(user.getPoint())
            .unwrittenReviewPoint(pointService.calculateMaximumPointForUnwrittenReview(userId))
            .reviewCount(reviewService.getReviewsByUserId(userId, userId).size())
            .orderList(orderService.getOrderPreviewsByUserIdAndStatus(userId, orderStatusList))
            .build();
    }

    public MyCouponsResponse getMyAllCoupons(Long userId) {
        findByUserId(userId);
        return MyCouponsResponse.builder()
            .couponList(userCouponService.getCouponsByUserId(userId))
            .build();
    }

    private User findByUserId(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }

    private List<KeepResponse> getKeepsByCategory(Long userId, Category category) {
        FindKeepByCategoryRequest request = FindKeepByCategoryRequest.builder()
            .userId(userId)
            .category(category)
            .build();

        return keepService.findKeepByUserAndCategory(userId, request);
    }

    private String findUsernameByUserId(Long userId) {
        String username = userQueryRepository.getUsername(userId);
        if (username == null) {
            throw new UserException(USERNAME_NOT_FOUND, String.valueOf(userId));
        }
        return username;
    }

}
