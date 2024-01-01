package com.programmers.smrtstore.domain.review.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.order.infrastructure.OrderJpaRepository;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJPARepository;
import com.programmers.smrtstore.domain.review.application.dto.req.CreateReviewRequest;
import com.programmers.smrtstore.domain.review.application.dto.req.ReviewLikeRequest;
import com.programmers.smrtstore.domain.review.application.dto.req.UpdateReviewRequest;
import com.programmers.smrtstore.domain.review.application.dto.res.CreateReviewResponse;
import com.programmers.smrtstore.domain.review.application.dto.res.ReviewResponse;
import com.programmers.smrtstore.domain.review.domain.entity.Review;
import com.programmers.smrtstore.domain.review.domain.entity.ReviewLike;
import com.programmers.smrtstore.domain.review.exception.ReviewException;
import com.programmers.smrtstore.domain.review.infrastructure.ReviewJpaRepository;
import com.programmers.smrtstore.domain.review.infrastructure.ReviewLikeJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewJpaRepository reviewJPARepository;
    private final UserRepository userRepository;
    private final ProductJPARepository productJPARepository;
    private final ReviewLikeJpaRepository reviewLikeJPARepository;
    private final OrderJpaRepository orderJpaRepository;

    public CreateReviewResponse createReview(CreateReviewRequest request) {
        if (!orderJpaRepository.verifyOrderDelivered(request.getUserId(), request.getProductId())) {
            throw new ReviewException(ErrorCode.REVIEW_NOT_EXIST_WHEN_NOT_ORDER_PRODUCT);
        }
        if (reviewJPARepository.validateReviewExist(request.getUserId(), request.getProductId())) {
            throw new ReviewException(ErrorCode.REVIEW_ALREADY_EXIST);
        }
        User user = getUser(request.getUserId());
        Product product = getProduct(request.getProductId());
        Review review = reviewJPARepository.save(Review.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .reviewScore(request.getReviewScore())
            .user(user)
            .product(product)
            .build());
        return CreateReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReviewById(Long reviewId) {
        Review review = getReview(reviewId);
        return ReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        Product product = getProduct(productId);
        return reviewJPARepository.findByProduct(product)
            .stream()
            .map(ReviewResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        User user = getUser(userId);
        return reviewJPARepository.findByUser(user)
            .stream()
            .map(ReviewResponse::from)
            .toList();
    }

    public ReviewResponse updateReview(UpdateReviewRequest request) {
        User user = getUser(request.getUserId());
        Review review = reviewJPARepository.findByIdAndUser(request.getReviewId(), user)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        review.updateValues(request.getTitle(), request.getContent(), request.getReviewScore());
        return ReviewResponse.from(review);
    }

    public Long deleteReview(Long reviewId) {
        Review review = getReview(reviewId);
        reviewJPARepository.delete(review);
        return review.getId();
    }

    public Long likeReview(ReviewLikeRequest request) {
        User user = getUser(request.getUserId());
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

    public Long dislikeReview(ReviewLikeRequest request) {
        User user = getUser(request.getUserId());
        Review review = getReview(request.getReviewId());
        ReviewLike reviewLike = reviewLikeJPARepository.findByUserAndReview(user, review)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_LIKE_NOT_FOUND));
        reviewLikeJPARepository.delete(reviewLike);
        return review.getId();
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
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
}
