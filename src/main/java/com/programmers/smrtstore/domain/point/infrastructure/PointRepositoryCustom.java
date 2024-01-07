package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import java.util.Optional;

public interface PointRepositoryCustom {

    Optional<PointResponse> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus);
    Optional<PointResponse> findByOrderIdAndPointStatus(Long orderId, PointStatus pointStatus);
}
