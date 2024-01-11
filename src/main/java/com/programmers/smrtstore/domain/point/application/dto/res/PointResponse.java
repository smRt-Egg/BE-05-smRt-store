package com.programmers.smrtstore.domain.point.application.dto.res;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointLabel;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PointResponse {

    private Long id;
    private Long userId;
    private Long orderId;
    private PointStatus status;
    private PointLabel label;
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
        this.label = point.getPointLabel();
        this.pointValue = point.getPointValue();
        this.issuedAt = point.getIssuedAt();
        this.expiredAt = point.getExpiredAt();
        this.membershipApplyYn = point.getMembershipApplyYn();
    }

    public static PointResponse from(Point point) {
        return new PointResponse(point);
    }
}
