package com.programmers.smrtstore.domain.order.infrastructure;

import static com.programmers.smrtstore.domain.order.domain.entity.QOrder.order;
import static com.programmers.smrtstore.domain.order.domain.entity.QOrderSheet.orderSheet;
import static com.programmers.smrtstore.domain.order.domain.entity.enums.OrderStatus.*;

import com.programmers.smrtstore.util.DateTimeUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
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
                .and(order.orderStatus.eq(PAYMENT_COMPLETED))
            )
            .fetchOne();

        return total != null ? total : 0;
    }

    @Override
    public Boolean existsOrderPurchaseConfirmed(Long userId, Long productId) {
        var result = queryFactory.selectFrom(order)
            .join(order.orderSheet, orderSheet)
            .where(orderSheet.user.id.eq(userId)
                .and(orderSheet.orderedProducts.any().product.id.eq(productId))
                .and(order.orderStatus.eq(PURCHASE_CONFIRMED))
            ).fetch();
        return !result.isEmpty();
    }

}
