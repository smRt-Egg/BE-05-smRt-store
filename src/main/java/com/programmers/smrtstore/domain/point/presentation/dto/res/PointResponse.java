package com.programmers.smrtstore.domain.point.presentation.dto.res;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.PointStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PointResponse {

    private Long id;
    private Long userId;
    private Long orderId;
    private PointStatus status;
    private Integer pointValue;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
    private Boolean membershipApplyYn;

    @Builder
    private PointResponse(Point point) {
        this.id = point.getId();
        this.userId = point.getUserId();
        this.orderId = point.getOrderId();
        this.status = point.getPointStatus();
        this.pointValue = point.getPointValue();
        this.issuedAt = point.getIssuedAt();
        this.expiredAt = point.getExpiredAt();
        this.membershipApplyYn = point.getMemberShipApplyYn();
    }

    public static PointResponse of(Point point) {
        return new PointResponse(point);
    }
}
