package com.programmers.smrtstore.domain.point.infrastructure;

import static com.programmers.smrtstore.domain.point.domain.entity.QPoint.point;
import static com.programmers.smrtstore.domain.point.domain.entity.QPointDetail.pointDetail;

import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailResponse;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointDetailRepositoryCustomImpl implements PointDetailRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PointDetailResponse> getUsedDetailsByOrderId(Long orderId) {
        return jpaQueryFactory.selectFrom(pointDetail)
            .where(pointDetail.pointId.in(
                JPAExpressions
                    .select(point.id)
                    .from(point)
                    .where(
                        point.orderId.eq(orderId),
                        point.pointStatus.eq(PointStatus.USED))
            ))
            .stream()
            .map(PointDetailResponse::from)
            .toList();
    }
}
