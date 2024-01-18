package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.review.application.dto.res.ReviewResponse;
import com.programmers.smrtstore.domain.review.domain.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyReviewsResponse {

    private String nickName;

    private String username;

    private Long orderDeliveryCount;

    private Integer couponCount;

    private Integer point;

    private List<ReviewResponse> reviewList;
}
