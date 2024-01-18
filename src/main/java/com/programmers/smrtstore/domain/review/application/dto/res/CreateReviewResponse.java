package com.programmers.smrtstore.domain.review.application.dto.res;

import com.programmers.smrtstore.domain.review.domain.entity.Review;
import com.programmers.smrtstore.domain.review.domain.entity.ReviewScore;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateReviewResponse {

    private Long id;
    private String title;
    private String content;
    private ReviewScore reviewScore;
    private Long userId;
    private Long orderedProductId;
    private LocalDateTime createdAt;

    public static CreateReviewResponse from(Review review) {
        return new CreateReviewResponse(
            review.getId(),
            review.getTitle(),
            review.getContent(),
            review.getReviewScore(),
            review.getUser().getId(),
            review.getOrderedProduct().getId(),
            review.getCreatedAt()
        );
    }
}
