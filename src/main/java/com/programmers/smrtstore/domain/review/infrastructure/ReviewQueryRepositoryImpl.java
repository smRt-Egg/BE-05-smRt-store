package com.programmers.smrtstore.domain.review.infrastructure;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import com.programmers.smrtstore.domain.order.domain.entity.QOrderedProduct;
import com.programmers.smrtstore.domain.order.orderSheet.domain.entity.QOrderSheet;
import com.programmers.smrtstore.domain.review.domain.entity.QReview;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean validateReviewExist(Long userId, Long productId) {
        Integer result = queryFactory
                .selectOne()
                .from(QReview.review)
                .where(QReview.review.user.id.eq(userId).and(QReview.review.product.id.eq(productId)))
                .fetchFirst();

        return result != null;
    }

    @Override
    public Long getUnWrittenReviewCount(Long userId) {
        Long count = queryFactory
                .select(QOrderSheet.orderSheet.id.count())
                .from(QOrderSheet.orderSheet)
                .leftJoin(QOrderSheet.orderSheet.orderedProducts, QOrderedProduct.orderedProduct)
                .leftJoin(QReview.review).on(QReview.review.product.id.eq(QOrderedProduct.orderedProduct.product.id))
                .where(QOrderSheet.orderSheet.user.id.eq(userId)
                        .and(QReview.review.isNull()))
                .fetchOne();
        return count != null ? count : 0;
    }


}
