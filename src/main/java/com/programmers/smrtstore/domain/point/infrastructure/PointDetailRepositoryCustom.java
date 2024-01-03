package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailResponse;
import java.util.List;

public interface PointDetailRepositoryCustom {

    List<PointDetailResponse> getUsedDetailsByOrderId(Long orderId);
}
