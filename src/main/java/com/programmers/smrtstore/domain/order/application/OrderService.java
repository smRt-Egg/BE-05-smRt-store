package com.programmers.smrtstore.domain.order.application;

import com.programmers.smrtstore.domain.order.presentation.dto.req.CreateOrderRequest;
import com.programmers.smrtstore.domain.order.presentation.dto.req.CreateOrderSheetRequest;
import com.programmers.smrtstore.domain.order.presentation.dto.req.UpdateOrderRequest;
import com.programmers.smrtstore.domain.order.presentation.dto.res.CreateOrderResponse;
import com.programmers.smrtstore.domain.order.presentation.dto.res.CreateOrderSheetResponse;
import com.programmers.smrtstore.domain.order.presentation.dto.res.OrderResponse;

public interface OrderService {

    CreateOrderSheetResponse createOrderSheet(CreateOrderSheetRequest request);

    CreateOrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(Long orderId);

    Long cancelOrder(Long orderId);

    Long updateOrder(Long orderId, UpdateOrderRequest request);

    Integer calculateUserMonthlyTotalSpending(Long userId, int month, int year);

    Integer getTotalPriceByOrderId(Long orderId);

}
