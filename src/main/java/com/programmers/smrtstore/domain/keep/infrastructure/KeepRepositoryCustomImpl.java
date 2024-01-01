package com.programmers.smrtstore.domain.keep.infrastructure;

import static com.programmers.smrtstore.domain.keep.domain.entity.QKeep.keep;
import static com.programmers.smrtstore.domain.product.domain.entity.QProduct.product;

import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

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

    @Override
    public List<KeepResponse> findKeepByUserAndCategory(Long userId, Category category) {
        return jpaQueryFactory.selectFrom(keep)
                .leftJoin(product).on(keep.productId.eq(product.id))
                .where(keep.userId.eq(userId),
                        product.category.eq(category))
                .stream().map(KeepResponse::of)
                .toList();
    }
}
