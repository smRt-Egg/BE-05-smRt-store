package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailCustomResponse;
import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import java.util.List;

public interface PointDetailRepositoryCustom {

    List<PointDetail> findUsedDetailsByOrderId(Long orderId);
    List<PointDetailCustomResponse> getSumGroupByOriginAcmId(Long userId);
}
