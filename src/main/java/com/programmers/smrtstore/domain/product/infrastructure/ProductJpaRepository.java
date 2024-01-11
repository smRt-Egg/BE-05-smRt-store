package com.programmers.smrtstore.domain.product.infrastructure;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    @Override
    @Query("select p from Product p where p.id = :id and p.deletedAt is null")
    Optional<Product> findById(Long id);

    @Override
    @Query("select p from Product p where p.deletedAt is null")
    List<Product> findAll();

    @Query("select p from Product p where p.id = ?1 and p.productStatusType = ?2")
    Optional<Product> findByIdAndProductStatusType(Long id, ProductStatusType productStatusType);

}