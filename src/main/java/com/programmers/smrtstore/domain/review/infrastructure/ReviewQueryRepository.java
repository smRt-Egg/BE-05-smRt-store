package com.programmers.smrtstore.domain.review.infrastructure;

import com.programmers.smrtstore.domain.review.domain.entity.Review;

import java.util.List;

public interface ReviewQueryRepository {

    Long getUnWrittenReviewCount(Long userId);
    List<Review> findByProductId(Long productId);
}
