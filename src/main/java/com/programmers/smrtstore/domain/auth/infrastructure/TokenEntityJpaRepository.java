package com.programmers.smrtstore.domain.auth.infrastructure;

import com.programmers.smrtstore.domain.auth.domain.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenEntityJpaRepository extends JpaRepository<TokenEntity, Long>,
    TokenEntityRepositoryCustom {

}