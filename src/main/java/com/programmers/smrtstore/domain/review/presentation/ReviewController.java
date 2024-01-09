package com.programmers.smrtstore.domain.review.presentation;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.review.application.ReviewService;
import com.programmers.smrtstore.domain.review.application.dto.req.CreateReviewRequest;
import com.programmers.smrtstore.domain.review.application.dto.req.ReviewLikeRequest;
import com.programmers.smrtstore.domain.review.application.dto.req.UpdateReviewRequest;
import com.programmers.smrtstore.domain.review.application.dto.res.CreateReviewResponse;
import com.programmers.smrtstore.domain.review.application.dto.res.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<CreateReviewResponse> createReview(@UserId Long securityUserId, @RequestBody CreateReviewRequest createReviewRequest) {
        CreateReviewResponse response = reviewService.createReview(createReviewRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> getReviewById(@UserId Long securityUserId, @PathVariable Long reviewId) {
        ReviewResponse response = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewsByProductId(@UserId Long securityUserId, @RequestParam("productId") Long productId) {
        List<ReviewResponse> response = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@UserId Long securityUserId, @RequestParam("userId") Long userId) {
        List<ReviewResponse> response = reviewService.getReviewsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ReviewResponse> updateReview(@UserId Long userId, @RequestBody UpdateReviewRequest updateReviewRequest) {
        ReviewResponse response = reviewService.updateReview(updateReviewRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Long> deleteReview(@UserId Long userId, @PathVariable Long reviewId) {
        Long response = reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/like")
    public ResponseEntity<Long> likeReview(@UserId Long userId, @RequestBody ReviewLikeRequest request) {
        Long response = reviewService.likeReview(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/like")
    public ResponseEntity<Long> dislikeReview(@UserId Long userId, @RequestBody ReviewLikeRequest request) {
        Long response = reviewService.dislikeReview(request);
        return ResponseEntity.ok(response);
    }
}
