package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import com.programmers.smrtstore.domain.point.exception.PointException;
import com.programmers.smrtstore.domain.point.infrastructure.PointDetailJpaRepository;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.req.PointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailCustomResponse;
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

    private final UserRepository userRepository;
    private final PointJpaRepository pointRepository;
    private final PointDetailJpaRepository pointDetailRepository;

    private Point getPointById(Long pointId) {
        return pointRepository.findById(pointId)
            .orElseThrow(() -> new PointException(ErrorCode.POINT_NOT_FOUND, String.valueOf(pointId)));
    }

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

        validateUserExists(request.getUserId());

        Long pointId = request.getPointId();
        Long userId = request.getUserId();

        List<PointDetailCustomResponse> history = pointDetailRepository.getSumGroupByOriginAcmId(userId);

        Point point = getPointById(pointId);
        int usedPoint = Math.abs(point.getPointValue());

        Long pointDetailId = null;
        for (PointDetailCustomResponse response : history) {
            while (usedPoint != 0) {
                int pointAmount = calculateDeductedPoint(response.getPointAmount(), usedPoint);
                PointDetail pointDetail = request.toEntity(-1 * pointAmount, response.getOriginAcmId());
                pointDetailRepository.save(pointDetail);
                usedPoint -= pointAmount;
                if (pointDetailId == null) pointDetailId = pointDetail.getId();
            }
        }
        return pointDetailId;
    }

    private int calculateDeductedPoint(int pointAmount, int usedPoint) {
        return Math.min(usedPoint, pointAmount);
    }

    public Long saveUseCancelHistory(PointDetailRequest request) {
        return null;
    }

    public Long saveExpirationHistory(Long userId) {
        return null;
    }
}
