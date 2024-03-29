package com.programmers.smrtstore.domain.orderManagement.order.infrastructure;


import static com.programmers.smrtstore.domain.orderManagement.order.domain.entity.QOrder.order;
import static com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus.PURCHASE_CONFIRMED;
import static com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.QOrderSheet.orderSheet;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus;
import com.programmers.smrtstore.util.DateTimeUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Integer calculateMonthlyTotalSpending(Long userId, int month, int year) {
        LocalDateTime[] boundaries = DateTimeUtils.getMonthBoundaries(month, year);
        LocalDateTime startDateTime = boundaries[0];
        LocalDateTime endDateTime = boundaries[1];

        Integer total = queryFactory
                .select(order.totalPrice.sum())
                .from(order)
                .join(order.orderSheet, orderSheet)
                .where(orderSheet.user.id.eq(userId)
                        .and(order.orderDate.between(startDateTime, endDateTime))
                        .and(order.orderStatus.eq(OrderStatus.PAYMENT_COMPLETED))
                )
                .fetchOne();

        return total != null ? total : 0;
    }

    @Override
    public Boolean existsOrderPurchaseConfirmed(Long userId, Long orderedProductId) {
        var result = queryFactory.selectFrom(order)
                .join(order.orderSheet, orderSheet)
                .where(orderSheet.user.id.eq(userId)
                        .and(orderSheet.orderedProducts.any().id.eq(orderedProductId))
                        .and(order.orderStatus.eq(PURCHASE_CONFIRMED))
                ).fetch();
        return !result.isEmpty();
    }

    @Override
    public Boolean existsOrder(Long userId, Long orderedProductId) {
        var result = queryFactory.selectFrom(order)
                .join(order.orderSheet, orderSheet)
                .where(orderSheet.user.id.eq(userId)
                        .and(orderSheet.orderedProducts.any().id.eq(orderedProductId))
                        .and(order.deletedAt.isNull())
                ).fetch();
        return !result.isEmpty();
    }

    @Override
    public Optional<Order> findByUserIdAndOrderedProductId(Long userId, Long orderedProductId) {
        return Optional.ofNullable(queryFactory.selectFrom(order)
                .join(order.orderSheet, orderSheet)
                .where(orderSheet.user.id.eq(userId)
                        .and(orderSheet.orderedProducts.any().id.eq(orderedProductId))
                        .and(order.deletedAt.isNull()))
                .fetchOne());
    }
}
