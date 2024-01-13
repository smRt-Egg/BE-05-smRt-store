package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import java.util.List;
import java.util.Optional;

public interface PointRepositoryCustom {

    List<Point> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus);
    Optional<Point> findUsedPointByOrderId(String orderId);
    List<Point> findByOrderIdAndPointStatus(String orderId, PointStatus pointStatus);
    Integer getAcmPointByOrderId(String orderId);
    Boolean findUserMembershipApplyYnByOrderId(String orderId);
    List<Point> findPointByPointStatusAndIssuedAt(Long userId,
        PointStatus pointStatus, int month, int year);
}
