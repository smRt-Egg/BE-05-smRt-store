package com.programmers.smrtstore.domain.review.application.dto.res;

import com.programmers.smrtstore.domain.review.domain.entity.Review;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewStatisticsResponse {

    private Integer totalReviewCount;
    private Long score1ReviewCount;
    private Long score2ReviewCount;
    private Long score3ReviewCount;
    private Long score4ReviewCount;
    private Long score5ReviewCount;
    private Double averageReviewScore;

    public static ReviewStatisticsResponse from(List<Review> reviews) {
        int totalReviewCount = reviews.size();
        long[] scoresCount = new long[5];

        if (!reviews.isEmpty()) {
            scoresCount = reviews.stream()
                .collect(Collectors.groupingBy(
                    review -> review.getReviewScore().getScore(),
                    Collectors.counting()
                ))
                .entrySet()
                .stream()
                .mapToLong(entry -> entry.getKey() == null ? 0 : entry.getValue())
                .toArray();
        }
        double averageReviewScore = totalReviewCount == 0 ? 0
            : reviews.stream().mapToDouble(review -> review.getReviewScore().getScore()).sum()
                / totalReviewCount;
        return new ReviewStatisticsResponse(
            totalReviewCount,
            scoresCount[0],
            scoresCount[1],
            scoresCount[2],
            scoresCount[3],
            scoresCount[4],
            averageReviewScore
        );

    }
}
