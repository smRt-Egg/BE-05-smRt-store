package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.req.PointRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.UsePointRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailResponse;
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
public class PointService {

    private final PointFacade pointFacade;
    private final UserRepository userRepository;
    private final PointJpaRepository pointRepository;

    public static final int MAX_AVAILALBE_USE_POINT = 2000000;

    public PointResponse accumulatePoint(PointRequest request) {
        return null;
    }

    public PointResponse cancelAccumulatedPoint(PointRequest request) {
        return null;
    }

    public PointResponse usePoint(UsePointRequest request) {
        return null;
    }

    public PointResponse cancelUsedPoint(PointRequest request) {

        validateUserExists(request.getUserId());

        PointResponse pointResponse = pointFacade.getByOrderIdAndStatus(
            request.getOrderId(),
            PointStatus.USED
        );

        Point point = request.toEntity(
            PointStatus.USE_CANCELED,
            pointResponse.getPointValue(),
            pointResponse.getMembershipApplyYn());
        pointRepository.save(point);

        int expiredPoint = calculateExpiredPoint(pointResponse.getOrderId());
        if (expiredPoint != 0) {
            Point expiredpoint = request.toEntity(
                PointStatus.EXPIRED,
                expiredPoint,
                pointResponse.getMembershipApplyYn());
            pointRepository.save(expiredpoint);
        }
        return PointResponse.from(point);
    }

    private int calculateExpiredPoint(Long orderId) {

        List<PointDetailResponse> usedDetailHistory = pointFacade.getUsedDetailByOrderId(orderId);

        int expiredPoint = 0;
        for (PointDetailResponse pointDetail : usedDetailHistory) {
            PointResponse originAcmPoint = pointFacade.getPointById(pointDetail.getPointId());
            if (pointFacade.validateExpiredAt(originAcmPoint)) {
                expiredPoint += pointDetail.getPointValue();
            }
        }
        return expiredPoint;
    }

    /**
     * 1. 주문 취소로 인한 포인트 재적립 과정에서 만료 처리 -> 즉시 만료 처리
     * 2. 00시 00분 00초에 일괄적으로 자동 만료 처리
     */

    public PointResponse expirePoint(Long userId) {
        return null;
    }

    public PointResponse getPointHistory(Long userId) {
        return null;
    }

    private User validateUserExists(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }
}
