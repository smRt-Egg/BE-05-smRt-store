package com.programmers.smrtstore.domain.qna.infrastructure;

import static com.programmers.smrtstore.domain.qna.domain.entity.QProductQuestion.productQuestion;
import static com.programmers.smrtstore.domain.qna.domain.entity.QProductAnswer.productAnswer;
import static com.programmers.smrtstore.domain.product.domain.entity.QProduct.product;

import com.programmers.smrtstore.domain.qna.presentation.dto.res.QuestionResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductQuestionRepositoryImpl implements ProductQuestionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<QuestionResponse> findByUserId(Long userId) {
        return jpaQueryFactory.select(Projections.constructor(QuestionResponse.class,
                        productQuestion.id,
                        productQuestion.userId,
                        productQuestion.productId,
                        product.name,
                        productQuestion.createdAt,
                        isAnswered(productQuestion.id)
                ))
                .from(productQuestion)
                .join(product).on(product.id.eq(productQuestion.productId))
                .where(productQuestion.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<QuestionResponse> findByProductId(Long productId) {
        return jpaQueryFactory.select(Projections.constructor(QuestionResponse.class,
                        productQuestion.id,
                        productQuestion.userId,
                        productQuestion.productId,
                        product.name,
                        productQuestion.createdAt,
                        isAnswered(productQuestion.id)
                ))
                .from(productQuestion)
                .join(product).on(product.id.eq(productQuestion.productId))
                .where(productQuestion.productId.eq(productId))
                .fetch();
    }

    BooleanExpression isAnswered(NumberPath<Long> questionId) {
        return jpaQueryFactory.select(Expressions.ONE)
                .from(productAnswer)
                .where(productAnswer.productQuestion.id.eq(questionId))
                .exists();
    }
}
