package com.programmers.smrtstore.domain.order.infrastructure;

public interface OrderRepositoryCustom {

    Integer calculateMonthlyTotalSpending(Long userId, int month, int year);

    Boolean existsOrderPurchaseConfirmed(Long userId, Long productId);
}
