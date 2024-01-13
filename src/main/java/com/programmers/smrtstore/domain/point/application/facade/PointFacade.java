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
    PointDetailResponse getAcmDetailByPointIdAndOriginAcmId(Long pointId);
    PointResponse getUsedPointByOrderId(String orderId);
    List<PointResponse> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus);
    List<PointResponse> getByOrderIdAndStatus(String orderId, PointStatus pointStatus);
    List<PointDetailResponse> getUsedDetailByPointId(Long pointId);
    List<PointDetailResponse> getUsedDetailByOrderId(String orderId);
    List<PointDetailResponse> getUsedDetailByPointIdAndOrderedProductId(Long pointId, Long orderedProductId);
    boolean validateExpiredAt(PointResponse pointResponse);
    Integer makeNegativeNumber(Integer pointAmount);
    List<ExpiredPointDetailResponse> getExpiredSumGroupByOriginAcmId();
    List<PointDetailCustomResponse> getSumGroupByOriginAcmId(Long userId);
    Integer getAcmPointByOrderId(String orderId);
    Integer getCancelPriceByPointIdAndOrderedProductId(Long pointId, Long orderedProductId);
    Boolean getUserMembershipApplyYnByOrderId(String orderId);
    List<PointResponse> getPointHistoryByIssuedAtAndStatus(
        Long userId,
        PointStatus pointStatus,
        TradeDateRange tradeDateRange);
}
