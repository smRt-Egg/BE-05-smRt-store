package com.programmers.smrtstore.domain.point.presentation.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsePointRequest {

    private Long userId;
    private Long orderId;
    private int pointAmount;

}
