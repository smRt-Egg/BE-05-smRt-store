package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsePointRequest {

    private Long userId;
    private Long orderId;
    private Integer pointAmount;

    public Point toEntity(boolean membershipApplyYn) {
        return Point.builder()
            .userId(userId)
            .orderId(orderId)
            .pointStatus(PointStatus.USED)
            .pointValue(-1 * pointAmount)
            .membershipApplyYn(membershipApplyYn)
            .build();
    }
}
