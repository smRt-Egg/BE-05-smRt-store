package com.programmers.smrtstore.domain.orderManagement.orderedProduct.infrastructure;

import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedProductJpaRepository extends JpaRepository<OrderedProduct, Long> {

}
