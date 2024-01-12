package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewPointDetailRequest {

    private Long pointId;
    private Long userId;
    private Long orderedProductId;

    public PointDetail toEntity() {
        return PointDetail.builder()
            .pointId(pointId)
            .userId(userId)
            .orderedProductId(orderedProductId)
            .pointAmount(PointService.REVIEW_POINT)
            .originAcmId(pointId)
            .build();
    }
}
