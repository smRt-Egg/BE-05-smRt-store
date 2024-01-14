package com.programmers.smrtstore.domain.point.application.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExpiredPointDetailResponse {

    private final Long originAcmId;
    private final Long userId;
    private final String orderId;
    private final Integer pointAmount;
    private final boolean membershipApplyYn;

    @Builder
    private ExpiredPointDetailResponse(Long originAcmId, Long userId, String orderId,
            Integer pointAmount, boolean membershipApplyYn) {
        this.originAcmId = originAcmId;
        this.userId = userId;
        this.orderId = orderId;
        this.pointAmount = pointAmount;
        this.membershipApplyYn = membershipApplyYn;
    }

    public static ExpiredPointDetailResponse of(Long originAcmId, Long userId, String orderId,
            Integer pointAmount, boolean membershipApplyYn) {
        return ExpiredPointDetailResponse.builder()
            .originAcmId(originAcmId)
            .userId(userId)
            .orderId(orderId)
            .pointAmount(pointAmount)
            .originAcmId(originAcmId)
            .membershipApplyYn(membershipApplyYn)
            .build();
    }
}
