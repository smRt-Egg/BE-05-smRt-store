package com.programmers.smrtstore.domain.user.presentation.dto.res;

import com.programmers.smrtstore.domain.review.application.dto.res.UnWrittenReviewResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyWritableReviewsResponse {

    private String nickName;

    private String username;

    private Long orderDeliveryCount;

    private Integer couponCount;

    private Integer point;

    private Integer unwrittenReviewPoint;

    private List<UnWrittenReviewResponse> reviewList;
}
