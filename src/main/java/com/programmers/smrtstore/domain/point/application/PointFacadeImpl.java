package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.exception.PointException;
import com.programmers.smrtstore.domain.point.infrastructure.PointDetailJpaRepository;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointFacadeImpl implements PointFacade {

    private final PointJpaRepository pointRepository;
    private final PointDetailJpaRepository pointDetailRepository;

    @Override
    public PointResponse getPointById(Long pointId) {
        Point point = pointRepository.findById(pointId)
            .orElseThrow(() -> new PointException(ErrorCode.POINT_NOT_FOUND));
        return PointResponse.from(point);
    }

    @Override
    public PointResponse getByOrderIdAndStatus(Long orderId, PointStatus pointStatus) {
        return pointRepository.findByOrderIdAndPointStatus(orderId, pointStatus)
            .orElseThrow(() -> new PointException(ErrorCode.POINT_NOT_FOUND));
    }

    @Override
    public List<PointDetailResponse> getUsedDetailByPointId(Long pointId) {
        return pointDetailRepository.findUsedDetailByPointId(pointId);
    }

    @Override
    public List<PointDetailResponse> getUsedDetailByOrderId(Long orderId) {
        return pointDetailRepository.findUsedDetailsByOrderId(orderId);
    }

    @Override
    public boolean validateExpiredAt(PointResponse pointResponse) {
        if (!pointResponse.getStatus().equals(PointStatus.ACCUMULATED)) {
            throw new UnsupportedOperationException();
        }
        return LocalDateTime.now().isAfter(pointResponse.getExpiredAt());
    }
}
