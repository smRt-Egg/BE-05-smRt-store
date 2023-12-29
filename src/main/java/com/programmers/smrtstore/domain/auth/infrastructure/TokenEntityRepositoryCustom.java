package com.programmers.smrtstore.domain.auth.infrastructure;

import com.programmers.smrtstore.domain.auth.domain.entity.TokenEntity;
import java.util.Optional;

public interface TokenEntityRepositoryCustom {

    Optional<TokenEntity> findByUsernameAndRefreshToken(String username, String refreshToken);

}
