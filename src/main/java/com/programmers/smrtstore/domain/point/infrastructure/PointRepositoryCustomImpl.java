package com.programmers.smrtstore.domain.point.infrastructure;

import static com.programmers.smrtstore.domain.point.domain.entity.QPoint.point;

import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PointRepositoryCustomImpl implements PointRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<PointResponse> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus) {
        return Optional.ofNullable(jpaQueryFactory
            .selectFrom(point)
            .where(
                point.orderId.in(
                    JPAExpressions
                        .select(point.orderId)
                        .from(point)
                        .where(point.id.eq(pointId))
                ),
                point.pointStatus.eq(pointStatus)
            )
            .fetchOne()
        ).map(PointResponse::from);
    }
}
