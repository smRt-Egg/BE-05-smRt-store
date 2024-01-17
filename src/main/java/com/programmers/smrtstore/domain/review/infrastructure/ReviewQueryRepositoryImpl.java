package com.programmers.smrtstore.domain.review.infrastructure;


import com.programmers.smrtstore.domain.review.application.dto.res.UnWrittenReviewResponse;
import com.programmers.smrtstore.domain.review.domain.entity.Review;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.QOrderSheet.orderSheet;
import static com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.QOrderedProduct.orderedProduct;
import static com.programmers.smrtstore.domain.review.domain.entity.QReview.review;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl implements ReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Long getUnWrittenReviewCount(Long userId) {
        Long count = queryFactory
                .select(orderSheet.id.count())
                .from(orderSheet)
                .leftJoin(orderSheet.orderedProducts, orderedProduct)
                .leftJoin(review).on(review.orderedProduct.eq(orderedProduct))
                .where(orderSheet.user.id.eq(userId)
                        .and(review.isNull()))
                .fetchOne();
        return count != null ? count : 0;
    }

    @Override
    public List<UnWrittenReviewResponse> getUnWrittenReviewResponses(Long userId) {
        return queryFactory.select(Projections.constructor(UnWrittenReviewResponse.class,
                        orderedProduct.product.name,
                        orderedProduct.product.contentImage))
                .from(orderSheet)
                .leftJoin(orderedProduct).on(orderSheet.orderedProducts.contains(orderedProduct))
                .leftJoin(review).on(orderedProduct.id.eq(review.orderedProduct.id).and(review.user.id.eq(userId)))
                .where(review.id.isNull())
                .fetch();
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        return queryFactory.selectFrom(review)
                .where(review.orderedProduct.product.id.eq(productId))
                .fetch();
    }

    @Override
    public List<Review> findByCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory.selectFrom(review)
                .where(review.createdAt.between(startDate, endDate),
                        review.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Review> findLikedReviewByCreatedAtBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return queryFactory.selectFrom(review)
                .where(review.reviewLikes.isNotEmpty(),
                        review.createdAt.between(startDate, endDate),
                        review.user.id.eq(userId))
                .fetch();
    }
}
