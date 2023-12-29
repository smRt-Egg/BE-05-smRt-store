package com.programmers.smrtstore.domain.auth.infrastructure;

import com.programmers.smrtstore.domain.auth.domain.entity.TokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenEntityJPARepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}