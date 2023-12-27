package com.programmers.smrtstore.domain.product.infrastructure;

import com.programmers.smrtstore.domain.product.domain.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionJPARepository extends JpaRepository<ProductOption, Long> {

}