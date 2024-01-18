package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.domain.point.application.PointService;
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
public class ReviewPointRequest {

    private Long userId;
    private String orderId;

    public Point toEntity(boolean membershipApplyYn) {
        return Point.builder()
            .userId(userId)
            .orderId(orderId)
            .pointStatus(PointStatus.ACCUMULATED)
            .pointLabel(PointLabel.REVIEW)
            .pointValue(PointService.REVIEW_POINT)
            .membershipApplyYn(membershipApplyYn)
            .build();
    }

}
