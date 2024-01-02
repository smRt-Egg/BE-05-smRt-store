package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.exception.PointException;
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
    private Long orderId;

    public Point toEntity(PointStatus pointStatus, Integer pointValue, boolean membershipApplyYn) {
        validatePointValue(pointStatus, pointValue);
        return Point.builder()
            .userId(userId)
            .orderId(orderId)
            .pointStatus(pointStatus)
            .pointValue(pointValue)
            .membershipApplyYn(membershipApplyYn)
            .build();
    }

    private static void validatePointValue(PointStatus pointStatus, Integer pointValue) {
        if (pointStatus.equals(PointStatus.ACCUMULATED) || pointStatus.equals(PointStatus.USE_CANCELED)) {
            if (pointValue < 0) {
                throw new PointException(ErrorCode.POINT_ILLEGAL_ARGUMENT, String.valueOf(pointValue));
            }
        } else {
            if (pointValue > 0) {
                throw new PointException(ErrorCode.POINT_ILLEGAL_ARGUMENT, String.valueOf(pointValue));
            }
        }
    }

}
