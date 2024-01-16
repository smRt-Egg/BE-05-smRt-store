package com.programmers.smrtstore.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyReviewsResponse {

    private String nickName;

    private String username;

    private Long orderDeliveryCount;

    private Integer couponCount;

    private Integer point;

    //기간 / 카테고리 조건 리뷰
}
