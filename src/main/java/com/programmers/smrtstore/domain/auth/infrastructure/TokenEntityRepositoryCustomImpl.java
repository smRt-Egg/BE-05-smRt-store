package com.programmers.smrtstore.domain.auth.infrastructure;

import static com.programmers.smrtstore.domain.auth.domain.entity.QAuth.auth;
import static com.programmers.smrtstore.domain.auth.domain.entity.QTokenEntity.tokenEntity;

import com.programmers.smrtstore.domain.auth.domain.entity.TokenEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenEntityRepositoryCustomImpl implements TokenEntityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TokenEntity> findByUsernameAndRefreshToken(String username,
        String refreshToken) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(tokenEntity)
            .leftJoin(auth).on(tokenEntity.auth.eq(auth))
            .where(auth.username.eq(username),
                tokenEntity.refreshToken.eq(refreshToken))
            .fetchFirst());
    }

    @Override
    public Optional<TokenEntity> findByUsername(String username) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(tokenEntity)
            .leftJoin(auth).on(tokenEntity.auth.eq(auth))
            .where(auth.username.eq(username))
            .fetchFirst());
    }
}
