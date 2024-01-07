package com.programmers.smrtstore.domain.point.infrastructure;

import static com.programmers.smrtstore.domain.point.domain.entity.QPoint.point;
import static com.programmers.smrtstore.domain.point.domain.entity.QPointDetail.pointDetail;

import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailCustomResponse;
import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointDetailRepositoryCustomImpl implements PointDetailRepositoryCustom {

    private JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PointDetail> findUsedDetailsByOrderId(Long orderId) {
        return jpaQueryFactory.selectFrom(pointDetail)
            .where(pointDetail.pointId.in(
                JPAExpressions
                    .select(point.id)
                    .from(point)
                    .where(
                        point.orderId.eq(orderId),
                        point.pointStatus.eq(PointStatus.USED))
            ))
            .fetch();
    }

    @Override
    public List<PointDetailCustomResponse> getSumGroupByOriginAcmId(Long userId) {
        return jpaQueryFactory
            .select(
                pointDetail.originAcmId,
                Expressions.numberTemplate(
                    Integer.class,
                    "SUM({0})", pointDetail.pointAmount
                ).as("pointAmount")
            )
            .from(pointDetail)
            .where(pointDetail.userId.eq(userId))
            .groupBy(pointDetail.originAcmId)
            .having(Expressions.numberTemplate(
                Integer.class,
                "SUM({0})", pointDetail.pointAmount)
                .gt(0))
            .orderBy(pointDetail.originAcmId.asc())
            .fetch()
            .stream()
            .map(tuple ->
                PointDetailCustomResponse.of(
                    tuple.get(pointDetail.originAcmId),
                    tuple.get(pointDetail.pointAmount))
            )
            .toList();
    }
}
