package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.domain.point.presentation.dto.req.FindPointByIssuedDateRequest;
import com.programmers.smrtstore.domain.point.presentation.dto.req.UsePointRequest;
import com.programmers.smrtstore.domain.point.presentation.dto.res.PointResponse;

public interface PointService {

    PointResponse earnPoint(Long userId, Long orderId);
    PointResponse usePoint(Long userId, Long orderId, UsePointRequest request);
    PointResponse cancelEarnedPoint(Long userId, Long orderId);
    PointResponse cancelUsedPoint(Long userId, Long orderId);
    PointResponse expirePoint(Long userId);
    PointResponse getPointByUserAndIssuedDate(FindPointByIssuedDateRequest request);
    PointResponse getPointHistory(Long userId);
}
