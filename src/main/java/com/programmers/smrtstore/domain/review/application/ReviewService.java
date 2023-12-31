package com.programmers.smrtstore.domain.review.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
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
import com.programmers.smrtstore.domain.review.infrastructure.ReviewJPARepository;
import com.programmers.smrtstore.domain.review.infrastructure.ReviewLikeJPARepository;
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

    private final ReviewJPARepository reviewJPARepository;
    private final UserRepository userRepository;
    private final ProductJPARepository productJPARepository;
    private final ReviewLikeJPARepository reviewLikeJPARepository;

    public CreateReviewResponse createReview(CreateReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, null));
        Product product = productJPARepository.findById(request.getProductId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
        reviewJPARepository.findByUserAndProduct(user, product)
            .ifPresent(review -> {
                throw new ReviewException(ErrorCode.REVIEW_ALREADY_EXIST);
            });
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
        Review review = reviewJPARepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        return ReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        Product product = productJPARepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));
        return reviewJPARepository.findByProduct(product)
            .stream()
            .map(ReviewResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, null));
        return reviewJPARepository.findByUser(user)
            .stream()
            .map(ReviewResponse::from)
            .toList();
    }

    public ReviewResponse updateReview(UpdateReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, null));
        Review review = reviewJPARepository.findByIdAndUser(request.getReviewId(), user)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        review.updateValues(request.getTitle(), request.getContent(), request.getReviewScore());
        return ReviewResponse.from(review);
    }

    public Long deleteReview(Long reviewId) {
        Review review = reviewJPARepository.findById(reviewId)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        reviewJPARepository.delete(review);
        return review.getId();
    }

    public Long likeReview(ReviewLikeRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, null));
        Review review = reviewJPARepository.findById(request.getReviewId())
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
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
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, null));
        Review review = reviewJPARepository.findById(request.getReviewId())
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));
        ReviewLike reviewLike = reviewLikeJPARepository.findByUserAndReview(user, review)
            .orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_LIKE_NOT_FOUND));
        reviewLikeJPARepository.delete(reviewLike);
        return review.getId();
    }

}
