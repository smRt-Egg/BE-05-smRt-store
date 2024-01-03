package com.programmers.smrtstore.domain.point.infrastructure;

import static com.programmers.smrtstore.domain.point.domain.entity.QPoint.point;

import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryCustomImpl implements PointRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<PointResponse> findByOrderIdAndPointStatus(Long orderId, PointStatus pointStatus) {
        return Optional.ofNullable(jpaQueryFactory
            .selectFrom(point)
            .where(
                point.orderId.eq(orderId),
                point.pointStatus.eq(pointStatus))
            .fetchOne())
            .map(PointResponse::from);
    }
}
