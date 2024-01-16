package com.programmers.smrtstore.domain.review.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.orderManagement.order.exception.OrderException;
import com.programmers.smrtstore.domain.orderManagement.order.infrastructure.OrderJpaRepository;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.exception.OrderedProductException;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.infrastructure.OrderedProductJpaRepository;
import com.programmers.smrtstore.domain.point.application.PointDetailService;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.point.application.dto.req.ReviewPointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.ReviewPointRequest;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.review.application.dto.req.CreateReviewRequest;
import com.programmers.smrtstore.domain.review.application.dto.req.ReviewLikeRequest;
import com.programmers.smrtstore.domain.review.application.dto.req.UpdateReviewRequest;
import com.programmers.smrtstore.domain.review.application.dto.res.CreateReviewResponse;
import com.programmers.smrtstore.domain.review.application.dto.res.ReviewResponse;
import com.programmers.smrtstore.domain.review.application.dto.res.ReviewStatisticsResponse;
import com.programmers.smrtstore.domain.review.domain.entity.Review;
import com.programmers.smrtstore.domain.review.domain.entity.ReviewLike;
import com.programmers.smrtstore.domain.review.exception.ReviewException;
import com.programmers.smrtstore.domain.review.infrastructure.ReviewJpaRepository;
import com.programmers.smrtstore.domain.review.infrastructure.ReviewLikeJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewJpaRepository reviewJPARepository;
    private final UserJpaRepository userJpaRepository;
    private final ProductJpaRepository productJPARepository;
    private final ReviewLikeJpaRepository reviewLikeJPARepository;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderedProductJpaRepository orderedProductJpaRepository;

    private final PointService pointService;
    private final PointDetailService pointDetailService;

    public CreateReviewResponse createReview(Long tokenUserId, CreateReviewRequest request) {
        // 주문 확정 상태가 아니라면 리뷰를 생성 불가
        if (!orderJpaRepository.existsOrderPurchaseConfirmed(request.getUserId(), request.getOrderProductId())) {
            throw new ReviewException(ErrorCode.REVIEW_NOT_EXIST_WHEN_NOT_ORDER_PRODUCT);
        }
        // 주문이 존재하지 않는다면 리뷰 생성 불가
        if (!orderJpaRepository.existsOrder(request.getUserId(), request.getOrderProductId())) {
            throw new ReviewException(ErrorCode.REVIEW_ALREADY_EXIST);
        }
        checkUserValid(tokenUserId, request.getUserId());
        User user = getUser(request.getUserId());
        OrderedProduct orderedProduct = getOrderedProduct(request.getOrderProductId());
        Order order = getOrder(request.getUserId(), request.getOrderProductId());
        Review review = reviewJPARepository.save(Review.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .reviewScore(request.getReviewScore())
                .user(user)
                .orderedProduct(orderedProduct)
                .build());
        accumulatePoint(review, order);
        return CreateReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long tokenUserId, Long reviewId) {
        User user = getUser(tokenUserId);
        Review review = getReview(reviewId);
        return ReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Long tokenUserId, Long productId) {
        User user = getUser(tokenUserId);
        return reviewJPARepository.findByProductId(productId)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }


    @Transactional(readOnly = true)
    public ReviewStatisticsResponse getReviewStatisticsFromProduct(Long productId) {
        return ReviewStatisticsResponse.from(reviewJPARepository.findByProductId(productId));
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Long tokenUserId, Long userId) {
        User user = getUser(userId);
        checkUserValid(tokenUserId, userId);
        return reviewJPARepository.findByUser(user)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    public ReviewResponse updateReview(Long tokenUserId, UpdateReviewRequest request) {
        User user = getUser(request.getUserId());
        checkUserValid(tokenUserId, request.getUserId());
        Review review = reviewJPARepository.findByIdAndUser(request.getReviewId(), user)
                .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        review.updateValues(request.getTitle(), request.getContent(), request.getReviewScore());
        return ReviewResponse.from(review);
    }

    public Long deleteReview(Long tokenUserId, Long reviewId) {
        User user = getUser(tokenUserId);
        Review review = getReview(reviewId);
        reviewJPARepository.delete(review);
        return review.getId();
    }

    public Long likeReview(Long tokenUserId, ReviewLikeRequest request) {
        User user = getUser(request.getUserId());
        checkUserValid(tokenUserId, request.getUserId());
        Review review = getReview(request.getReviewId());
        reviewLikeJPARepository.findByUserAndReview(user, review).ifPresent(reviewLike -> {
            throw new ReviewException(ErrorCode.REVIEW_LIKE_ALREADY_EXIST);
        });
        reviewLikeJPARepository.save(ReviewLike.builder()
                .user(user)
                .review(review)
                .build());
        return review.getId();
    }

    public Long dislikeReview(Long tokenUserId, ReviewLikeRequest request) {
        User user = getUser(request.getUserId());
        checkUserValid(tokenUserId, request.getUserId());
        Review review = getReview(request.getReviewId());
        ReviewLike reviewLike = reviewLikeJPARepository.findByUserAndReview(user, review)
                .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_LIKE_NOT_FOUND));
        review.removeReviewLike(reviewLike);
        return review.getId();
    }

    public Long getUnWrittenReviewCount(Long userId) {
        return reviewJPARepository.getUnWrittenReviewCount(userId);
    }

    private User getUser(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, null));
    }

    private Product getProduct(Long productId) {
        return productJPARepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    private Review getReview(Long reviewId) {
        return reviewJPARepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
    }

    private Order getOrder(Long userId, Long orderedProductId) {
        return orderJpaRepository.findByUserIdAndOrderedProductId(userId, orderedProductId)
                .orElseThrow(() -> new OrderException(ErrorCode.ORDER_NOT_FOUND));
    }

    private OrderedProduct getOrderedProduct(Long orderedProductId) {
        return orderedProductJpaRepository.findById(orderedProductId)
                .orElseThrow(() -> new OrderedProductException(ErrorCode.ORDERED_PRODUCT_MISMATCH_ERROR));
    }

    private void checkUserValid(Long tokenUserId, Long requestUserId) {
        if (!tokenUserId.equals(requestUserId)) {
            throw new ReviewException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private Integer accumulatePoint(Review review, Order order) {
        ReviewPointRequest reviewPointRequest = ReviewPointRequest.builder()
                .userId(review.getUser().getId())
                .orderId(order.getId())
                .build();
        Long pointId = pointService.accumulatePointByReview(reviewPointRequest);
        ReviewPointDetailRequest reviewPointDetailRequest = ReviewPointDetailRequest.builder()
                .pointId(pointId)
                .userId(review.getUser().getId())
                .orderedProductId(review.getOrderedProduct().getId())
                .build();
        Integer pointAmount = pointDetailService.saveReviewAcmHistory(reviewPointDetailRequest);
        return pointAmount;
    }
}
