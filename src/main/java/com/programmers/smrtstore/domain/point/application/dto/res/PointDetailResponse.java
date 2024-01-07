package com.programmers.smrtstore.domain.point.application.dto.res;

import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PointDetailResponse {

    private Long id;
    private Long pointId;
    private Long userId;
    private Integer pointValue;
    private Long originAcmId;

    @Builder
    private PointDetailResponse(PointDetail pointDetail) {
        this.id = pointDetail.getId();
        this.pointId = pointDetail.getPointId();
        this.userId = pointDetail.getUserId();
        this.pointValue = pointDetail.getPointAmount();
        this.originAcmId = pointDetail.getOriginAcmId();
    }

    public static PointDetailResponse from(PointDetail pointDetail) {
        return new PointDetailResponse(pointDetail);
    }
}
