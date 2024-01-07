package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.domain.entity.vo.TradeDateRange;
import com.programmers.smrtstore.domain.point.infrastructure.PointDetailJpaRepository;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PointFacadeImpl implements PointFacade {

    private final PointJpaRepository pointRepository;
    private final PointDetailJpaRepository pointDetailRepository;

    @Override
    public List<PointResponse> getPointHistoryByIssuedAtAndStatus(Long userId, PointStatus pointStatus, TradeDateRange tradeDateRange) {
        return pointRepository.findPointByPointStatusAndIssuedAt(
            userId,
            pointStatus,
            tradeDateRange.getYear(),
            tradeDateRange.getMonth());
    }
}
