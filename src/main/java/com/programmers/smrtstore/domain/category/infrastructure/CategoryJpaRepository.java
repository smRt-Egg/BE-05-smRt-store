package com.programmers.smrtstore.domain.category.infrastructure;

import com.programmers.smrtstore.domain.category.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

}