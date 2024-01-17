package com.programmers.smrtstore.domain.review.infrastructure;

import com.programmers.smrtstore.domain.review.application.dto.res.UnWrittenReviewResponse;
import com.programmers.smrtstore.domain.review.domain.entity.Review;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewQueryRepository {

    Long getUnWrittenReviewCount(Long userId);
    List<UnWrittenReviewResponse> getUnWrittenReviewResponses(Long userId);
    List<Review> findByProductId(Long productId);
    List<Review> findByCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    List<Review> findLikedReviewByCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}
