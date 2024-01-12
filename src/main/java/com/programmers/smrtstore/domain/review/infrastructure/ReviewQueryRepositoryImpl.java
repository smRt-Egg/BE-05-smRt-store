package com.programmers.smrtstore.domain.review.infrastructure;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.QOrderSheet.orderSheet;
import static com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.QOrderedProduct.orderedProduct;
import static com.programmers.smrtstore.domain.review.domain.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean validateReviewExist(Long userId, Long productId) {
        Integer result = queryFactory
                .selectOne()
                .from(review)
                .where(review.user.id.eq(userId).and(review.product.id.eq(productId)))
                .fetchFirst();

        return result != null;
    }

    @Override
    public Long getUnWrittenReviewCount(Long userId) {
        Long count = queryFactory
                .select(orderSheet.id.count())
                .from(orderSheet)
                .leftJoin(orderSheet.orderedProducts, orderedProduct)
                .leftJoin(review).on(review.product.id.eq(orderedProduct.product.id))
                .where(orderSheet.user.id.eq(userId)
                        .and(review.isNull()))
                .fetchOne();
        return count != null ? count : 0;
    }

}
