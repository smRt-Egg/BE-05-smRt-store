package com.programmers.smrtstore.domain.point.application;


import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.application.dto.req.PointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import com.programmers.smrtstore.domain.point.infrastructure.PointDetailJpaRepository;
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
public class PointDetailService {

    private final PointFacade pointFacade;
    private final UserRepository userRepository;
    private final PointDetailJpaRepository pointDetailRepository;

    public Long saveAccumulationHistory(PointDetailRequest request) {
        return null;
    }

    public Long saveAccumulationCancelHistory(PointDetailRequest request) {
        return null;
    }

    public Long saveUseHistory(PointDetailRequest request) {
        return null;
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
                if (pointDetailId == null) pointDetailId = pointDetail.getId();

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

    public Long saveExpirationHistory(Long userId) {
        return null;
    }

    private User validateUserExists(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }
}
