package com.programmers.smrtstore.domain.point.infrastructure;

import static com.programmers.smrtstore.domain.point.domain.entity.QPoint.point;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.util.DateTimeUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryCustomImpl implements PointRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Point> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus) {
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
        );
    }

    @Override
    public Optional<Point> findByOrderIdAndPointStatus(Long orderId, PointStatus pointStatus) {
        return Optional.ofNullable(jpaQueryFactory
            .selectFrom(point)
            .where(
                point.orderId.eq(orderId),
                point.pointStatus.eq(pointStatus)
            )
            .fetchOne()
        );
    }

    @Override
    public List<PointResponse> findPointByPointStatusAndIssuedAt(Long userId, PointStatus pointStatus, int month, int year) {
        LocalDateTime[] boundaries = DateTimeUtils.getMonthBoundaries(month, year);
        LocalDateTime startDateTime = boundaries[0];
        LocalDateTime endDateTime = boundaries[1];

        return jpaQueryFactory
            .selectFrom(point)
            .where(
                point.userId.eq(userId),
                point.pointStatus.eq(pointStatus),
                point.expiredAt.between(startDateTime, endDateTime)
            )
            .fetch()
            .stream()
            .map(PointResponse::from)
            .toList();
    }
}
