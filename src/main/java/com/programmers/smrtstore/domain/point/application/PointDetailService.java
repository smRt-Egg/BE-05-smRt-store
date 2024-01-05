package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.application.dto.res.ExpiredPointDetailResponse;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import com.programmers.smrtstore.domain.point.exception.PointException;
import com.programmers.smrtstore.domain.point.infrastructure.PointDetailJpaRepository;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.req.PointDetailRequest;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointDetailService {

    private final PointFacade pointFacade;
    private final UserRepository userRepository;
    private final PointJpaRepository pointRepository;
    private final PointDetailJpaRepository pointDetailRepository;

    public Long saveAccumulationHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        Long pointId = request.getPointId();

        Point point = pointRepository.findById(pointId)
            .orElseThrow(() -> new PointException(ErrorCode.POINT_NOT_FOUND, String.valueOf(pointId)));
        PointDetail pointDetail = request.toEntity(point);
        pointDetailRepository.save(pointDetail);
        return pointDetail.getId();
    }

    private User validateUserExists(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }

    public Long saveAccumulationCancelHistory(PointDetailRequest request) {
        return null;
    }

    public Long saveUseHistory(PointDetailRequest request) {
        return null;
    }

    public Long saveUseCancelHistory(PointDetailRequest request) {
        return null;
    }

    public Long saveExpirationHistory() {

        List<ExpiredPointDetailResponse> expireHistory = pointFacade.getExpiredSumGroupByOriginAcmId();

        Long pointDetailId = null;
        for (ExpiredPointDetailResponse expireDetail : expireHistory) {
            PointDetail pointDetail = PointDetail.builder()
                .pointId(null)
                .userId(expireDetail.getUserId())
                .pointAmount(expireDetail.getPointAmount() * -1)
                .originAcmId(expireDetail.getOriginAcmId())
                .build();
            pointDetailRepository.save(pointDetail);
            if (pointDetailId == null) pointDetailId = pointDetail.getId();
        }
        return pointDetailId;
    }
}
