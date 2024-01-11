package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import java.util.List;
import java.util.Optional;

public interface PointRepositoryCustom {

    List<Point> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus);
    Optional<Point> findUsedPointByOrderId(Long orderId);
    List<Point> findByOrderIdAndPointStatus(Long orderId, PointStatus pointStatus);
    Integer getAcmPointByOrderId(Long orderId);
    Boolean findUserMembershipApplyYnByOrderId(Long orderId);
}
