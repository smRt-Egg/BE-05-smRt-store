package com.programmers.smrtstore.domain.orderManagement.order.application;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req.CreateOrderRequest;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req.UpdateOrderRequest;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderPreviewResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderedProductResponse;
import java.util.List;

public interface OrderService {

    String createOrder(Long userId, CreateOrderRequest request);

    OrderResponse getOrderById(Long orderId);

    Long cancelOrder(Long orderId);

    Long updateOrder(Long orderId, UpdateOrderRequest request);

    List<OrderPreviewResponse> getOrderPreviewsByUserId(Long userId);

    List<OrderPreviewResponse> getOrderPreviewsByUserIdAndStatus(Long userId, List<OrderStatus> statuses);

    Long getActiveOrderCountByUserId(Long userId);



}
