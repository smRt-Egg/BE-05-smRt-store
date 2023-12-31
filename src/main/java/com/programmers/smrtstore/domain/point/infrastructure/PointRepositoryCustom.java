package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import java.util.Optional;

public interface PointRepositoryCustom {

    Optional<Point> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus);
    Optional<Point> findByOrderIdAndPointStatus(Long orderId, PointStatus pointStatus);
}
