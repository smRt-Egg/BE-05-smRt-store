package com.programmers.smrtstore.domain.keep.infrastructure;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.programmers.smrtstore.domain.keep.domain.entity.QKeep.keep;
import static com.programmers.smrtstore.domain.product.domain.entity.QProduct.product;

@RequiredArgsConstructor
public class KeepRepositoryCustomImpl implements KeepRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<KeepRankingResponse> findTopProductIdsWithCount(int limit) {
        return jpaQueryFactory.select(Projections.constructor(KeepRankingResponse.class, keep.product.id, keep.product.id.count()))
                .from(keep)
                .groupBy(keep.product.id)
                .orderBy(keep.product.id.count().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<KeepResponse> findKeepByUserAndCategory(Long userId, Category category) {
        List<Keep> keepList = jpaQueryFactory.selectFrom(keep)
                .leftJoin(product).on(keep.product.id.eq(product.id))
                .fetchJoin()
                .where(keep.userId.eq(userId),
                        product.category.eq(category))
                .fetch();
        return keepList.stream().map(k -> new KeepResponse(
                k.getId(),
                k.getUserId(),
                k.getProduct().getName(),
                k.getProduct().getSalePrice(),
                k.getProduct().getContentImage()
        )).toList();
    }

    @Override
    public List<KeepResponse> findAllByUserId(Long userId) {
        List<Keep> keepList = jpaQueryFactory.selectFrom(keep)
                .leftJoin(product).on(keep.product.id.eq(product.id))
                .fetchJoin()
                .where(keep.userId.eq(userId))
                .fetch();
        return keepList.stream().map(k -> new KeepResponse(
                k.getId(),
                k.getUserId(),
                k.getProduct().getName(),
                k.getProduct().getSalePrice(),
                k.getProduct().getContentImage()
        )).toList();
    }
}
