package com.programmers.smrtstore.domain.product.infrastructure;

import com.programmers.smrtstore.domain.product.domain.entity.ProductQuantity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {

}