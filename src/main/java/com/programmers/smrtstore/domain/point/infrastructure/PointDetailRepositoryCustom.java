package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import com.querydsl.core.Tuple;
import java.util.List;

public interface PointDetailRepositoryCustom {

    List<PointDetail> findUsedDetailsByOrderId(Long orderId);
    List<Tuple> getSumGroupByOriginAcmId(Long userId);
    List<Tuple> getExpiredSumGroupByOriginAcmId();
}
