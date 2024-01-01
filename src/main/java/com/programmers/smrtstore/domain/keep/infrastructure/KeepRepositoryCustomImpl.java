package com.programmers.smrtstore.domain.keep.infrastructure;

import static com.programmers.smrtstore.domain.keep.domain.entity.QKeep.keep;
import static com.programmers.smrtstore.domain.product.domain.entity.QProduct.product;

import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Category;
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

    @Override
    public List<KeepResponse> findKeepByUser(Long userId) {
        return jpaQueryFactory.select(Projections.constructor(KeepResponse.class, keep.id,
                        keep.userId,
                        product.name,
                        product.salePrice,
                        product.contentImage))
                .from(keep)
                .leftJoin(product).on(keep.productId.eq(product.id))
                .where(keep.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<KeepResponse> findKeepByUserAndCategory(Long userId, Category category) {
        return jpaQueryFactory.select(Projections.constructor(KeepResponse.class, keep.id,
                        keep.userId,
                        product.name,
                        product.salePrice,
                        product.contentImage))
                .from(keep)
                .join(product).on(keep.productId.eq(product.id))
                .where(keep.userId.eq(userId),
                        product.category.eq(category))
                .fetch();
    }
}
