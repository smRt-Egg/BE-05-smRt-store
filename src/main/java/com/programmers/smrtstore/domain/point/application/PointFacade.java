package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.domain.entity.vo.TradeDateRange;
import java.util.List;

public interface PointFacade {

    List<PointResponse> getPointHistoryByIssuedAtAndStatus(
        Long userId,
        PointStatus pointStatus,
        TradeDateRange tradeDateRange);

}
