package com.programmers.smrtstore.domain.review.infrastructure;

public interface ReviewQueryRepository {

    Boolean validateReviewExist(Long userId, Long productId);

}
