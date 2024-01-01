package com.programmers.smrtstore.domain.review.infrastructure;

import static com.programmers.smrtstore.domain.review.domain.entity.QReview.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewQueryRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public Boolean validateReviewExist(Long userId, Long productId) {
        return !queryFactory.selectFrom(review)
            .where(review.user.id.eq(userId), review.product.id.eq(productId))
            .fetch().isEmpty();
    }

}
