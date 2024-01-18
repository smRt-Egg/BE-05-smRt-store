package com.programmers.smrtstore.domain.review.application.dto.req;

import com.programmers.smrtstore.domain.review.domain.entity.ReviewScore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateReviewRequest {

    private String title;
    private String content;
    private ReviewScore reviewScore;
    private Long userId;
    private Long orderProductId;
}
