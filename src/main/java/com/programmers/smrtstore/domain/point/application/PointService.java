package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.domain.point.presentation.dto.req.FindPointByIssuedDateRequest;
import com.programmers.smrtstore.domain.point.presentation.dto.req.PointRequest;
import com.programmers.smrtstore.domain.point.presentation.dto.req.UsePointRequest;
import com.programmers.smrtstore.domain.point.presentation.dto.res.PointResponse;

public interface PointService {

    PointResponse earnPoint(PointRequest request);
    PointResponse usePoint(UsePointRequest request);
    PointResponse cancelEarnedPoint(PointRequest request);
    PointResponse cancelUsedPoint(PointRequest request);
    PointResponse expirePoint(Long userId);
    PointResponse getPointByUserAndIssuedDate(FindPointByIssuedDateRequest request);
    PointResponse getPointHistory(Long userId);
}
