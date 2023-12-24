package com.programmers.smrtstore.domain.keep.infrastructure;

import static com.programmers.smrtstore.domain.keep.domain.entity.QKeep.keep;

import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class KeepRepositoryCustomImpl implements KeepRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KeepRankingResponse> findTopProductIdsWithCount(int limit) {
       return jpaQueryFactory.select(Projections.constructor(KeepRankingResponse.class, keep.productId, keep.productId.count()))
               .from(keep)
               .groupBy(keep.productId)
               .orderBy(keep.productId.count().desc())
               .limit(limit)
               .fetch();
    }
}
