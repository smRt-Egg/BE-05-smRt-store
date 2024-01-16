package com.programmers.smrtstore.domain.orderManagement.order.application;

import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.CreateOrderResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req.CreateOrderRequest;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req.UpdateOrderRequest;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderPreviewResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderedProductResponse;
import java.util.List;

public interface OrderService {

    CreateOrderResponse createOrder(CreateOrderRequest request);

    OrderResponse getOrderById(Long orderId);

    Long cancelOrder(Long orderId);

    Long updateOrder(Long orderId, UpdateOrderRequest request);

    Integer calculateUserMonthlyTotalSpending(Long userId, int month, int year);

    Integer getTotalPriceByOrderId(String orderId);

    List<OrderedProductResponse> getProductsForOrder(String orderId);

    List<OrderPreviewResponse> getOrderPreviewsByUserId(Long userId);

    Long getActiveOrderCountByUserId(Long userId);

}
