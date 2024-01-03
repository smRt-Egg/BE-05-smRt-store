package com.programmers.smrtstore.domain.auth.infrastructure;

import com.programmers.smrtstore.domain.auth.domain.entity.Auth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthJpaRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByUsername(String username);

    Optional<Auth> findByUserId(Long userId);
}