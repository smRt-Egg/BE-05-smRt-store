package com.programmers.smrtstore.domain.point.application;


public interface PointDetailService {

    Long saveEarnHistory(Long userId, Long pointId);
    Long saveUseHistory(Long userId, Long pointId);
    Long saveEarnCancelHistory(Long userId, Long pointId);
    Long saveUseCancelHistory(Long userId, Long pointId);
    Long saveExpirationHistory(Long userId);
}
