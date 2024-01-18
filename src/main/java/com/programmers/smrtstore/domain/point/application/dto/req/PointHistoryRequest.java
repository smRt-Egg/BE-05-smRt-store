package com.programmers.smrtstore.domain.point.application.dto.req;

import com.programmers.smrtstore.domain.point.domain.entity.vo.TradeDateRange;
import com.programmers.smrtstore.domain.point.domain.entity.enums.TradeType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointHistoryRequest {

    private Long userId;
    private TradeType tradeType;
    private TradeDateRange tradeDateRange;

}
