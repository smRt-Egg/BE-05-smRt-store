package com.programmers.smrtstore.domain.user.infrastructure;

import static com.programmers.smrtstore.domain.auth.domain.entity.QAuth.auth;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public String getUsername(Long userId) {
        return queryFactory
            .select(auth.username)
            .from(auth)
            .where(auth.user.id.eq(userId))
            .fetchFirst();
    }
}
