package com.programmers.smrtstore.domain.review.infrastructure;

import static com.programmers.smrtstore.domain.review.domain.entity.QReview.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
