package com.programmers.smrtstore.domain.order.infrastructure;

import com.programmers.smrtstore.domain.order.domain.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderJpaRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    // TODO: user 가 탈퇴한 경우 어떻게 처리할 것인가?
    @EntityGraph(attributePaths = {"orderSheet", "orderSheet.user"})
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.deletedAt IS NULL")
    Optional<Order> findByIdWithOrderSheetAndUser(Long orderId);

    @EntityGraph(attributePaths = {"orderSheet"})
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.deletedAt IS NULL")
    Optional<Order> findByIdWithOrderSheet(Long orderId);

    @Override
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.deletedAt IS NULL")
    Optional<Order> findById(Long orderId);

    @EntityGraph(attributePaths = {"orderSheet"})
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> findByIdWithOrderSheetIncludeDeleted(Long orderId);

    @Query("SELECT o FROM Order o WHERE o.id = :orderIdL")
    Optional<Order> findByIdIncludeDeleted(Long orderIdL);

}
