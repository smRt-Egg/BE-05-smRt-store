package com.programmers.smrtstore.domain.point.application;


import com.programmers.smrtstore.domain.point.presentation.dto.req.PointDetailRequest;

public interface PointDetailService {

    Long saveEarnHistory(PointDetailRequest request);
    Long saveUseHistory(PointDetailRequest request);
    Long saveEarnCancelHistory(PointDetailRequest request);
    Long saveUseCancelHistory(PointDetailRequest request);
    Long saveExpirationHistory(Long userId);
}
