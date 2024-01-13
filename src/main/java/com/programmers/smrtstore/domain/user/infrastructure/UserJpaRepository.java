package com.programmers.smrtstore.domain.user.infrastructure;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Override
    @Query("select u from User u where u.deletedAt is null and u.id = :id")
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
