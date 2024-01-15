package com.programmers.smrtstore.domain.point.presentation.dto.req;

import com.programmers.smrtstore.domain.point.domain.entity.vo.TradeDateRange;
import com.programmers.smrtstore.domain.point.domain.entity.enums.TradeType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PointHistoryAPIRequest {

    @NotNull
    private Long userId;

    @NotNull
    private TradeType tradeType;

    @NotNull
    private TradeDateRange tradeDateRange;

}
