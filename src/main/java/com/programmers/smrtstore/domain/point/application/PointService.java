package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.application.dto.req.PointRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.exception.PointException;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.req.UsePointRequest;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final UserRepository userRepository;
    private final PointJpaRepository pointRepository;

    public static final int MAX_AVAILALBE_USE_POINT = 2000000;

    @Transactional(readOnly = true)
    public PointResponse getPointById(Long pointId) {
        Point point = pointRepository.findById(pointId)
            .orElseThrow(() -> new PointException(ErrorCode.POINT_NOT_FOUND));
        return PointResponse.from(point);
    }

    public PointResponse accumulatePoint(PointRequest request) {
        return null;
    }

    private User validateUserExists(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }

    public PointResponse cancelAccumulatedPoint(PointRequest request) {
        return null;
    }

    public PointResponse usePoint(UsePointRequest request) {

        User user = validateUserExists(request.getUserId());

        Point point = request.toEntity(user.isMembershipYN());
        pointRepository.save(point);
        return PointResponse.from(point);
    }

    public PointResponse cancelUsedPoint(PointRequest request) {
        return null;
    }

    public PointResponse expirePoint(Long userId) {
        return null;
    }

    public PointResponse getPointHistory(Long userId) {
        return null;
    }
}
