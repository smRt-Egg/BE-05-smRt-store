package com.programmers.smrtstore.domain.category.infrastructure;

import com.programmers.smrtstore.domain.category.domain.entity.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByValue(String value);
}