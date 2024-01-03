package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
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
public class PointDetailRequest {

    private Long userId;
    private Long pointId;

    public PointDetail toEntity(Point point) {
        return PointDetail.builder()
            .pointId(pointId)
            .userId(userId)
            .pointAmount(point.getPointValue())
            .originAcmId(pointId)
            .build();
    }
}
