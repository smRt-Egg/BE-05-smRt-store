package com.programmers.smrtstore.domain.order.application;

import com.programmers.smrtstore.domain.order.presentation.dto.req.CreateOrderRequest;
import com.programmers.smrtstore.domain.order.presentation.dto.req.UpdateOrderRequest;
import com.programmers.smrtstore.domain.order.presentation.dto.res.CreateOrderResponse;
import com.programmers.smrtstore.domain.order.presentation.dto.res.OrderResponse;
import com.programmers.smrtstore.domain.order.presentation.dto.res.OrderedProductResponse;
import java.util.List;

public interface OrderService {

    CreateOrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(Long orderId);

    Long cancelOrder(Long orderId);

    Long updateOrder(Long orderId, UpdateOrderRequest request);

    Integer calculateUserMonthlyTotalSpending(Long userId, int month, int year);

    Integer getTotalPriceByOrderId(Long orderId);

    List<OrderedProductResponse> getProductsForOrder(Long orderId);

}
