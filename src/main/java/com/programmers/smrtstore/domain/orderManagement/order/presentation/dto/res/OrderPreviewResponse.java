package com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderPreviewResponse {
    private String orderId;
    private String orderDate;
    private String orderStatus;
    private Integer orderTotalPrice;

    @Builder
    public OrderPreviewResponse(
        String orderId, String orderDate, String orderStatus, Integer orderTotalPrice
    ) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.orderTotalPrice = orderTotalPrice;
    }

    public static OrderPreviewResponse from(
        Order order
    ) {
        return OrderPreviewResponse.builder()
            .orderId(order.getId())
            .orderDate(order.getOrderDate().toString())
            .orderStatus(order.getOrderStatus().toString())
            .orderTotalPrice(order.getTotalPrice())
            .build();
    }
}
