package com.programmers.smrtstore.domain.review.application.dto.res;

import com.programmers.smrtstore.domain.review.domain.entity.Review;
import com.programmers.smrtstore.domain.review.domain.entity.ReviewScore;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewResponse {

    private Long id;
    private String title;
    private String content;
    private ReviewScore reviewScore;
    private Long userId;
    private Long productId;
    private Integer likeCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public static ReviewResponse from(Review review) {
        return new ReviewResponse(review.getId(),
            review.getTitle(),
            review.getContent(),
            review.getReviewScore(),
            review.getUser().getId(),
            review.getProduct().getId(),
            review.getReviewLikeCount(),
            review.getCreatedAt(),
            review.getUpdatedAt());
    }
}
