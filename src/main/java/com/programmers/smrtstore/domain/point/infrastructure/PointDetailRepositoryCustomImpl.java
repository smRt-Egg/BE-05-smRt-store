package com.programmers.smrtstore.domain.point.infrastructure;

import static com.programmers.smrtstore.domain.point.domain.entity.QPoint.point;
import static com.programmers.smrtstore.domain.point.domain.entity.QPointDetail.pointDetail;

import com.programmers.smrtstore.domain.point.domain.entity.PointDetail;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.querydsl.core.Tuple;
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

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PointDetail> findUsedDetailsByOrderId(String orderId) {
        return jpaQueryFactory.selectFrom(pointDetail)
            .where(pointDetail.pointId.in(
                JPAExpressions
                    .select(point.id)
                    .from(point)
                    .where(
                        point.orderId.eq(orderId),
                        point.pointStatus.eq(PointStatus.USED)
                    )
            ))
            .fetch();
    }

    @Override
    public List<Tuple> getSumGroupByOriginAcmId(Long userId) {

        return jpaQueryFactory
            .select(
                pointDetail.originAcmId,
                pointDetail.pointAmount.sum()
            )
            .from(pointDetail)
            .where(pointDetail.userId.eq(userId))
            .groupBy(pointDetail.originAcmId)
            .having(Expressions.numberTemplate(
                Integer.class,
                "SUM({0})", pointDetail.pointAmount
            ).gt(0))
            .orderBy(pointDetail.originAcmId.asc())
            .fetch();
    }

    @Override
    public List<Tuple> getExpiredSumGroupByOriginAcmId() {

        LocalDate today = LocalDate.now();

        return jpaQueryFactory
            .select(
                pointDetail.originAcmId,
                point.userId,
                point.orderId,
                pointDetail.pointAmount.sum(),
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
            .fetch();
    }

    @Override
    public List<PointDetail> getUsedDetailByPointIdAndOrderedProductId(Long pointId, Long orderedProductid) {
        return jpaQueryFactory
            .selectFrom(pointDetail)
            .where(
                pointDetail.pointId.in(
                    JPAExpressions
                        .select(point.id)
                        .from(point)
                        .where(point.pointStatus.eq(PointStatus.USED))
                ),
                pointDetail.orderedProductId.eq(orderedProductid)
            )
            .fetch();
    }

    @Override
    public Integer getTotalPriceByPointIdAndOrderedProductId(Long pointId, Long orderedProductid) {
        return jpaQueryFactory
            .select(pointDetail.pointAmount.sum())
            .from(pointDetail)
            .where(
                pointDetail.pointId.eq(pointId),
                pointDetail.orderedProductId.eq(orderedProductid)
            )
            .fetchOne();
    }
}
