package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AcmPointDetailRequest {

    private Long userId;
    private String orderId;
    private List<Long> orderedProductIds;

    public PointDetail toEntity(Long pointId, Long orderedProductId, Integer pointAmount, Long originAcmId) {
        return PointDetail.builder()
            .pointId(pointId)
            .orderedProductId(orderedProductId)
            .userId(userId)
            .pointAmount(pointAmount)
            .originAcmId(originAcmId)
            .build();
    }
}
