package com.programmers.smrtstore.domain.order.infrastructure;

import com.programmers.smrtstore.domain.order.domain.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    @EntityGraph(attributePaths = {"orderSheet", "orderSheet.user"})
    Optional<Order> findByIdWithOrderSheetAndUser(Long orderId);
}
