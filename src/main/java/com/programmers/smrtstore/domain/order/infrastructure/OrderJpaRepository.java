package com.programmers.smrtstore.domain.order.infrastructure;

import com.programmers.smrtstore.domain.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

}
