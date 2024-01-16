package com.programmers.smrtstore.domain.user.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyWritableReviewsResponse {

    private String nickName;

    private String username;

    private Long orderDeliveryCount;

    private Integer couponCount;

    private Integer point;

    private Integer unwrittenReviewPoint;

//    작성 안한 리뷰 리스트
}
