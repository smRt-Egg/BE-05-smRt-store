package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.exception.PointException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsePointRequest {

    private Long userId;
    private Long orderId;
    private int pointAmount;

    public Point toEntity(boolean membershipApplyYn) {
        validatePointValue(pointAmount);
        return Point.builder()
            .userId(userId)
            .orderId(orderId)
            .pointStatus(PointStatus.USED)
            .pointValue(-1 * pointAmount)
            .membershipApplyYn(membershipApplyYn)
            .build();
    }

    private static void validatePointValue(Integer pointValue) {
        if (pointValue > 0) {
            throw new PointException(ErrorCode.POINT_ILLEGAL_ARGUMENT, String.valueOf(pointValue));
        }

        if (Math.abs(pointValue) > PointService.MAX_AVAILALBE_USE_POINT) {
            throw new PointException(ErrorCode.POINT_AVAILABLE_RANGE_EXCEED, String.valueOf(pointValue));
        }
    }
}
