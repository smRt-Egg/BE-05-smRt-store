package com.programmers.smrtstore.domain.point.infrastructure;

import static com.programmers.smrtstore.domain.point.domain.entity.QPointDetail.pointDetail;
import static com.programmers.smrtstore.domain.point.domain.entity.QPoint.point;

import com.programmers.smrtstore.domain.point.application.dto.res.ExpiredPointDetailResponse;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PointDetailRepositoryCustomImpl implements PointDetailRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ExpiredPointDetailResponse> getExpiredSumGroupByOriginAcmId() {

        LocalDate today = LocalDate.now();

        return jpaQueryFactory
            .select(
                pointDetail.originAcmId,
                point.userId,
                point.orderId,
                pointDetail.pointAmount.sum().as("total"),
                point.membershipApplyYn
            )
            .from(pointDetail)
            .innerJoin(point).on(pointDetail.pointId.eq(point.id))
            .where(pointDetail.originAcmId.in(
                JPAExpressions
                    .select(point.id)
                    .from(point)
                    .where(point.expiredAt.eq(
                        Expressions.dateTemplate(LocalDate.class, "{0}", today))
                    )
                )
            )
            .groupBy(
                pointDetail.originAcmId,
                point.userId,
                point.orderId,
                point.membershipApplyYn
            )
            .having(pointDetail.pointAmount.sum().gt(0))
            .orderBy(pointDetail.originAcmId.asc())
            .fetch()
            .stream()
            .map(tuple ->
                ExpiredPointDetailResponse.of(
                    tuple.get(pointDetail.originAcmId),
                    tuple.get(point.userId),
                    tuple.get(point.orderId),
                    tuple.get(3, Integer.class),
                    Boolean.TRUE.equals(tuple.get(point.membershipApplyYn))
                ))
            .toList();
    }
}
