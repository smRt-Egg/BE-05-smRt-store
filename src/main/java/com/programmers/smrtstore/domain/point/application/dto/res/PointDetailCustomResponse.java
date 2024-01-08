package com.programmers.smrtstore.domain.point.application.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PointDetailCustomResponse {

    private Long originAcmId;
    private Integer pointAmount;

    @Builder
    private PointDetailCustomResponse(Long originAcmId, Integer pointAmount) {
        this.originAcmId = originAcmId;
        this.pointAmount = pointAmount;
    }

    public static PointDetailCustomResponse of(Long originAcmId, Integer pointAmount) {
        return new PointDetailCustomResponse(originAcmId, pointAmount);
    }
}
