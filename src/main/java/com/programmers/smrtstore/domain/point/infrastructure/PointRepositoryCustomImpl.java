package com.programmers.smrtstore.domain.point.infrastructure;

import static com.programmers.smrtstore.domain.point.domain.entity.QPoint.point;

import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.util.DateTimeUtils;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryCustomImpl implements PointRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Point> findByPointIdAndPointStatus(Long pointId, PointStatus pointStatus) {
        return jpaQueryFactory
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
            .fetch();
    }

    @Override
    public Optional<Point> findUsedPointByOrderId(String orderId) {
        return Optional.ofNullable(jpaQueryFactory
            .selectFrom(point)
            .where(
                point.orderId.eq(orderId),
                point.pointStatus.eq(PointStatus.USED)
            )
            .fetchOne()
        );
    }

    @Override
    public List<Point> findByOrderIdAndPointStatus(String orderId, PointStatus pointStatus) {
        return jpaQueryFactory
            .selectFrom(point)
            .where(
                point.orderId.eq(orderId),
                point.pointStatus.eq(pointStatus)
            )
            .fetch();
    }

    @Override
    public Integer getAcmPointByOrderId(String orderId) {
        return jpaQueryFactory
            .select(point.pointValue.sum().as("totalAcmPoint"))
            .from(point)
            .where(
                point.orderId.eq(orderId),
                point.pointStatus.eq(PointStatus.ACCUMULATED)
            )
            .fetchOne();

    }

    @Override
    public Boolean findUserMembershipApplyYnByOrderId(String orderId) {
        return jpaQueryFactory
            .select(point.membershipApplyYn)
            .from(point)
            .where(
                point.orderId.eq(orderId),
                point.pointStatus.eq(PointStatus.ACCUMULATED)
            )
            .fetchOne();
    }

    @Override
    public List<Point> findPointByPointStatusAndIssuedAt(Long userId, PointStatus pointStatus, int month, int year) {
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
            .fetch();
    }
}
