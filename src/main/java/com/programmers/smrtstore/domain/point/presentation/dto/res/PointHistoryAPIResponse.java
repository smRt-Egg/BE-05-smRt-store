package com.programmers.smrtstore.domain.point.presentation.dto.res;

import com.programmers.smrtstore.domain.point.application.dto.res.PointHistoryResponse;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.domain.entity.enums.TradeType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PointHistoryAPIResponse {

    private Long pointId;
    private String title;
    private Integer pointAmount;
    private LocalDateTime tradeDateTime;
    private TradeType tradeType;
    private Boolean buyAcmTradeYn;
    private String orderId;

    public static PointHistoryAPIResponse from(PointHistoryResponse response) {
        return new PointHistoryAPIResponse(
            response.getId(),
            response.getName(),
            response.getPointValue(),
            response.getIssuedAt(),
            setTradeType(response.getStatus()),
            setBuyAcmTradeYn(response.getStatus()),
            response.getOrderId()
        );
    }

    private static Boolean setBuyAcmTradeYn(PointStatus pointStatus) {
        return pointStatus.equals(PointStatus.ACCUMULATED);
    }

    private static TradeType setTradeType(PointStatus pointStatus) {
        if (pointStatus == null) {
            return TradeType.ALL;
        } else if (pointStatus.equals(PointStatus.ACCUMULATED)) {
            return TradeType.ACCUMULATED;
        }
        return TradeType.USED;
    }
}
