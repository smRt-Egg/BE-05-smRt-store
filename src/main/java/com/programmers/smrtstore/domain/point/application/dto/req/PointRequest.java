package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointLabel;
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
public class PointRequest {

    private Long userId;
    private String orderId;

    public Point toEntity(PointStatus pointStatus, Integer pointValue, boolean membershipApplyYn) {
        return Point.builder()
            .userId(userId)
            .orderId(orderId)
            .pointStatus(pointStatus)
            .pointLabel(PointLabel.ORDER)
            .pointValue(pointValue)
            .membershipApplyYn(membershipApplyYn)
            .build();
    }
}
