package com.programmers.smrtstore.domain.orderManagement.order.infrastructure;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;

import java.util.Optional;

public interface OrderRepositoryCustom {

    Integer calculateMonthlyTotalSpending(Long userId, int month, int year);

    Boolean existsOrderPurchaseConfirmed(Long userId, Long orderedProductId);

    Boolean existsOrder(Long userId, Long orderedProductId);

    Optional<Order> findByUserIdAndOrderedProductId(Long userId, Long orderedProductId);
}
