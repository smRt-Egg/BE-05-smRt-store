package com.programmers.smrtstore.domain.review.application.dto.res;

import com.programmers.smrtstore.domain.review.domain.entity.Review;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewDetailResponse {

    private Integer size;
    private Float avgScore;

    public static ReviewDetailResponse from(List<Review> products) {
        return new ReviewDetailResponse(
            products.size(),
            products.isEmpty() ? 0f
                : (float) products.stream().map(review -> review.getReviewScore().getScore())
                    .reduce(0, Integer::sum) / products.size());
    }
}
