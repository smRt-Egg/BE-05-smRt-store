package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailCustomResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailResponse;
import com.programmers.smrtstore.domain.point.domain.entity.vo.TradeDateRange;
import com.programmers.smrtstore.domain.point.application.dto.res.ExpiredPointDetailResponse;
import java.util.List;

public interface PointFacade {

    PointResponse getPointById(Long pointId);
    PointResponse getByOrderIdAndStatus(Long orderId, PointStatus pointStatus);
    List<PointDetailResponse> getUsedDetailByPointId(Long pointId);
    List<PointDetailResponse> getUsedDetailByOrderId(Long orderId);
    boolean validateExpiredAt(PointResponse pointResponse);
    Integer makeNegativeNumber(Integer pointAmount);
    List<ExpiredPointDetailResponse> getExpiredSumGroupByOriginAcmId();
    List<PointDetailCustomResponse> getSumGroupByOriginAcmId(Long userId);
    List<PointResponse> getPointHistoryByIssuedAtAndStatus(
        Long userId,
        PointStatus pointStatus,
        TradeDateRange tradeDateRange);

}
