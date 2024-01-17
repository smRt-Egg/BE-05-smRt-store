package com.programmers.smrtstore.domain.user.application.service;

import static com.programmers.smrtstore.core.properties.ErrorCode.EMAIL_VERIFICATION_CODE_ERROR;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_DUPLICATE_EMAIL;
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
import com.programmers.smrtstore.domain.user.presentation.dto.req.DurationRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyAllKeepsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyCategoryKeepsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyHomeResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyOrdersResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyQnaResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyReviewsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.MyWritableReviewsResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final RedisService redisService;
    private final MailService mailService;
    private final CartService cartService;
    private final UserCouponService userCouponService;
    private final KeepService keepService;
    private final OrderServiceImpl orderService;
    private final PointService pointService;
    private final ReviewService reviewService;
    private static final String MESSAGE_TITLE = "smRt store 인증 번호";

    private static final String VERIFICATION_CODE_PRIFIX = "VerificationCode ";
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Transactional(readOnly = true)
    public ProfileUserResponse getUserInfo(Long userId) {
        User user = findByUserId(userId);
        return ProfileUserResponse.from(user);
    }

    private User findByUserId(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }

    public ProfileUserResponse update(Long userId, UpdateUserRequest request) {
        User user = findByUserId(userId);
        user.updateUser(request);
        return ProfileUserResponse.from(user);
    }

    public void withdraw(Long userId) {
        User user = findByUserId(userId);
        user.saveDeleteDate();
    }

    public int updatePoint(Long userId, int pointValue) {
        User user = findByUserId(userId);
        return user.updatePoint(pointValue);
    }

    public String sendCodeToEmail(String userEmail) {
        this.checkDuplicatedEmail(userEmail);
        String certificationCode = createCode();
        mailService.sendEmail(userEmail, MESSAGE_TITLE, certificationCode);
        redisService.setValues(VERIFICATION_CODE_PRIFIX + userEmail,
            certificationCode, Duration.ofMillis(authCodeExpirationMillis));
        return certificationCode;
    }

    @Transactional(readOnly = true)
    public void verifyCode(String userEmail, String code) {
        String savedCode = redisService.getValues(VERIFICATION_CODE_PRIFIX + userEmail);
        boolean verifyResult = redisService.checkExistsValue(savedCode) && savedCode.equals(code);
        if (!verifyResult) {
            throw new UserException(EMAIL_VERIFICATION_CODE_ERROR, code);
        }
    }

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
        return MyOrdersResponse.builder()
            .nickName(user.getNickName())
            .username(user.getAuth().getUsername())
            .point(user.getPoint())
            .unwrittenReviewPoint(pointService.calculateMaximumPointForUnwrittenReview(userId))
            .reviewCount(reviewService.getReviewsByUserId(userId, userId).size())
            .orderList(orderService.getOrderPreviewsByUserId(userId))
            .build();
    }

    @Transactional(readOnly = true)
    public MyAllKeepsResponse getMyAllKeeps(Long userId) {
        User user = findByUserId(userId);
        return MyAllKeepsResponse.builder()
            .nickName(user.getNickName())
            .username(user.getAuth().getUsername())
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            .allKeeps(keepService.getAllKeepsByUserId(userId, userId))
            .build();
    }

    @Transactional(readOnly = true)
    public MyCategoryKeepsResponse getMyKeepsByCategory(Long userId, Integer categoryId) {
        User user = findByUserId(userId);
        return MyCategoryKeepsResponse.builder()
            .nickName(user.getNickName())
            .username(user.getAuth().getUsername())
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            .categoryKeeps(getKeepsByCategory(userId, Category.findById(categoryId)))
            .build();
    }

    @Transactional(readOnly = true)
    public MyReviewsResponse getMyReviews(Long userId, DurationRequest request) {
        User user = findByUserId(userId);
        return MyReviewsResponse.builder()
            .nickName(user.getNickName())
            .username(user.getAuth().getUsername())
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            //리뷰 리스트
            .build();
    }
    @Transactional(readOnly = true)
    public MyWritableReviewsResponse getMyWritableReviews(Long userId) {
        User user = findByUserId(userId);
        return MyWritableReviewsResponse.builder()
            .nickName(user.getNickName())
            .username(user.getAuth().getUsername())
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            //작성 안 한 리뷰 리스트
            .build();
    }

    public MyQnaResponse getMyQna(Long userId, DurationRequest request) {
        User user = findByUserId(userId);
        return MyQnaResponse.builder()
            .nickName(user.getNickName())
            .username(user.getAuth().getUsername())
            .orderDeliveryCount(orderService.getActiveOrderCountByUserId(userId))
            .couponCount(userCouponService.getCouponsByUserId(userId).size())
            .point(user.getPoint())
            //qna 리스트
            .build();
    }

    public MyOrdersResponse getPurchasedConfirmedOrders(Long userId) {
        User user = findByUserId(userId);
        List<OrderStatus> orderStatusList = new ArrayList<>();
        orderStatusList.add(PURCHASE_CONFIRMED);
        return MyOrdersResponse.builder()
            .nickName(user.getNickName())
            .username(user.getAuth().getUsername())
            .point(user.getPoint())
            .unwrittenReviewPoint(pointService.calculateMaximumPointForUnwrittenReview(userId))
            .reviewCount(reviewService.getReviewsByUserId(userId, userId).size())
            .orderList(orderService.getOrderPreviewsByUserIdAndStatus(userId, orderStatusList))
            .build();
    }

    private List<KeepResponse> getKeepsByCategory(Long userId, Category category) {
        FindKeepByCategoryRequest request = FindKeepByCategoryRequest.builder()
            .userId(userId)
            .category(category)
            .build();

        return keepService.findKeepByUserAndCategory(userId, request);
    }

    private void checkDuplicatedEmail(String userEmail) {
        userJpaRepository.findByEmail(userEmail).ifPresent(e -> {
            throw new UserException(USER_DUPLICATE_EMAIL, userEmail);
        });
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ignore) {
            return null;
        }
    }
}