package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.presentation.dto.res.PointDetailCustomResponse;
import java.util.List;

public interface PointDetailRepositoryCustom {

    List<PointDetailCustomResponse> getSumGroupByOriginAcmId(Long userId);
}
