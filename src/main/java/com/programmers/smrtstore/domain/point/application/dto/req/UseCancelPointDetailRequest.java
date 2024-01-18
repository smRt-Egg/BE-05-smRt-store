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
public class UseCancelPointDetailRequest {

    private Long pointId;
    private Long userId;
    private List<Long> orderedProductIds; // 취소할 주문상품의 번호

    public PointDetail toEntity(Long orderedProductId, Integer pointAmount, Long originAcmId) {
        return PointDetail.builder()
            .pointId(pointId)
            .orderedProductId(orderedProductId)
            .userId(userId)
            .pointAmount(pointAmount)
            .originAcmId(originAcmId)
            .build();
    }

}
