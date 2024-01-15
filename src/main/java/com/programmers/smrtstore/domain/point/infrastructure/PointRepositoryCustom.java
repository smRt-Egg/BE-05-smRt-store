package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.domain.entity.enums.TradeType;
import com.querydsl.core.Tuple;
import java.util.List;
import java.util.Optional;

public interface PointRepositoryCustom {

    List<Point> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus);
    Optional<Point> findUsedPointByOrderId(String orderId);
    List<Point> findByOrderIdAndPointStatus(String orderId, PointStatus pointStatus);
    Integer getAcmPointByOrderId(String orderId);
    Boolean findUserMembershipApplyYnByOrderId(String orderId);
    List<Tuple> findPointHisoryByPointStatusAndIssuedAt(Long userId,
        TradeType tradeType, int month, int year);
}
