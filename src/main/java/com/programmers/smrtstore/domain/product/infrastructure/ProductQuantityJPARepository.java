package com.programmers.smrtstore.domain.product.infrastructure;

import com.programmers.smrtstore.domain.product.domain.entity.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQuantityJPARepository extends JpaRepository<ProductQuantity, Long> {

}