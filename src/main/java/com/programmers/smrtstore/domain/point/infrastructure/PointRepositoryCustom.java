package com.programmers.smrtstore.domain.point.infrastructure;

import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import java.util.List;

public interface PointRepositoryCustom {

    List<PointResponse> findPointByPointStatusAndIssuedAt(Long userId, PointStatus pointStatus, int month, int year);

}
