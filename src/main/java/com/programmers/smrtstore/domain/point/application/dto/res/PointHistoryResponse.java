package com.programmers.smrtstore.domain.point.application.dto.res;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointLabel;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.domain.entity.enums.TradeType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PointHistoryResponse {

    private Long id;
    private Long userId;
    private String orderId;
    private String name;
    private PointStatus status;
    private PointLabel label;
    private TradeType tradeType;
    private Integer pointValue;
    private LocalDateTime issuedAt;
    private LocalDateTime expiredAt;
    private Boolean membershipApplyYn;

    @Builder
    private PointHistoryResponse(Point point, String name, TradeType tradeType) {
        this.id = point.getId();
        this.userId = point.getUserId();
        this.orderId = point.getOrderId();
        this.name = name;
        this.status = point.getPointStatus();
        this.label = point.getPointLabel();
        this.tradeType = tradeType;
        this.pointValue = point.getPointValue();
        this.issuedAt = point.getIssuedAt();
        this.expiredAt = point.getExpiredAt();
        this.membershipApplyYn = point.getMembershipApplyYn();
    }

    public static PointHistoryResponse of(Point point, String name, TradeType tradeType) {
        return new PointHistoryResponse(point, name, tradeType);
    }
}
