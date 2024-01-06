package com.programmers.smrtstore.domain.orderManagement.order.infrastructure;

public interface OrderRepositoryCustom {

    Integer calculateMonthlyTotalSpending(Long userId, int month, int year);

    Boolean verifyOrderDelivered(Long userId, Long productId);
}
