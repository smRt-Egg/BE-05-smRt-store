package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import java.util.Optional;

public interface PointRepositoryCustom {

    Optional<PointResponse> findByOrderIdAndPointStatus(Long orderId, PointStatus pointStatus);
}
