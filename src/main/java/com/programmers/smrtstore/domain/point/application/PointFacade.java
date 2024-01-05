package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.domain.point.application.dto.res.ExpiredPointDetailResponse;
import java.util.List;

public interface PointFacade {

    List<ExpiredPointDetailResponse> getExpiredSumGroupByOriginAcmId();
}
