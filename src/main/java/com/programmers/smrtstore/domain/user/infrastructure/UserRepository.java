package com.programmers.smrtstore.domain.user.infrastructure;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByLoginId(String loginId);
}
