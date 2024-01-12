package com.programmers.smrtstore.domain.product.infrastructure;

import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailOptionJpaRepository extends JpaRepository<ProductDetailOption, Long> {

    @Override
    @Query("select o from ProductDetailOption o where o.id = ?1 and o.deletedAt is null")
    Optional<ProductDetailOption> findById(Long id);
}