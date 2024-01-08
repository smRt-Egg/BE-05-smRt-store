package com.programmers.smrtstore.domain.point.application;


import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.application.dto.res.ExpiredPointDetailResponse;
import com.programmers.smrtstore.domain.point.application.dto.req.PointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.exception.PointException;
import com.programmers.smrtstore.domain.point.infrastructure.PointDetailJpaRepository;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailCustomResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailResponse;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointDetailService {

    private final PointFacade pointFacade;
    private final UserJpaRepository userJpaRepository;
    private final PointJpaRepository pointRepository;
    private final PointDetailJpaRepository pointDetailRepository;

    public Long saveAccumulationHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        Long pointId = request.getPointId();
        PointResponse point = pointFacade.getPointById(pointId);
        PointDetail pointDetail = request.toEntity(point.getPointValue(), pointId);
        pointDetailRepository.save(pointDetail);
        return pointDetail.getId();
    }

    public PointResponse getByPointIdAndStatus(Long pointId, PointStatus pointStatus) {
        Point point =  pointRepository.findByPointIdAndPointStatus(pointId, pointStatus)
            .orElseThrow(() -> new PointException(ErrorCode.POINT_NOT_FOUND));
        return PointResponse.from(point);
    }

    public Long saveAccumulationCancelHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        Long pointId = request.getPointId();
        PointResponse pointResponse = getByPointIdAndStatus(pointId, PointStatus.ACCUMULATED);

        PointDetail pointDetail = request.toEntity(pointResponse.getPointValue(), pointResponse.getId());
        pointDetailRepository.save(pointDetail);
        return pointDetail.getPointId();
    }

    public Long saveUseHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        Long pointId = request.getPointId();
        Long userId = request.getUserId();

        List<PointDetailCustomResponse> history = pointDetailRepository.getSumGroupByOriginAcmId(userId);

        PointResponse point = pointFacade.getPointById(pointId);
        int usedPoint = Math.abs(point.getPointValue());

        Long pointDetailId = null;
        for (PointDetailCustomResponse response : history) {
            while (usedPoint != 0) {
                int pointAmount = calculateDeductedPoint(response.getPointAmount(), usedPoint);
                PointDetail pointDetail = request.toEntity(
                    pointFacade.makeNegativeNumber(pointAmount),
                    response.getOriginAcmId()
                );
                pointDetailRepository.save(pointDetail);
                usedPoint -= pointAmount;
                if (pointDetailId == null) {
                    pointDetailId = pointDetail.getId();
                }
            }
        }
        return pointDetailId;
    }

    private int calculateDeductedPoint(int pointAmount, int usedPoint) {
        return Math.min(usedPoint, pointAmount);
    }

    public Long saveUseCancelHistory(PointDetailRequest request) {

        validateUserExists(request.getUserId());

        PointResponse point = pointFacade.getPointById(request.getPointId());
        int canceledPoint = Math.abs(point.getPointValue());

        List<PointDetailResponse> usedDetailHistory = pointFacade.getUsedDetailByOrderId(point.getOrderId());

        Long pointDetailId = null;
        for (PointDetailResponse pointDetail : usedDetailHistory) {
            while (canceledPoint != 0) {

                PointDetail canceledDetail = request.toEntity(
                    Math.abs(pointDetail.getPointValue()),
                    pointDetail.getOriginAcmId());
                pointDetailRepository.save(canceledDetail);
                if (pointDetailId == null) {
                    pointDetailId = pointDetail.getId();
                }

                PointResponse originAcmPoint = pointFacade.getPointById(request.getPointId());
                if (pointFacade.validateExpiredAt(originAcmPoint)) {
                    PointDetail expiredDetail = request.toEntity(
                        pointDetail.getPointValue(),
                        pointDetail.getOriginAcmId());
                    pointDetailRepository.save(expiredDetail);
                }
                canceledPoint += pointDetail.getPointValue();
            }
        }
        return pointDetailId;
    }

    public Long saveExpirationHistory() {

        List<ExpiredPointDetailResponse> expireHistory = pointFacade.getExpiredSumGroupByOriginAcmId();

        Long pointDetailId = null;
        for (ExpiredPointDetailResponse expireDetail : expireHistory) {
            PointDetail pointDetail = PointDetail.builder()
                .pointId(null)
                .userId(expireDetail.getUserId())
                .pointAmount(pointFacade.makeNegativeNumber(expireDetail.getPointAmount()))
                .originAcmId(expireDetail.getOriginAcmId())
                .build();
            pointDetailRepository.save(pointDetail);
            if (pointDetailId == null) pointDetailId = pointDetail.getId();
        }
        return pointDetailId;
    }

    private User validateUserExists(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }
}
