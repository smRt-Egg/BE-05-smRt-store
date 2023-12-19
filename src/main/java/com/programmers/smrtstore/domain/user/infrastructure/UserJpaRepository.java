package com.programmers.smrtstore.domain.user.infrastructure;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}
