package com.programmers.smrtstore.domain.orderManagement.order.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.ORDER_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.orderManagement.order.exception.OrderException;
import com.programmers.smrtstore.domain.orderManagement.order.infrastructure.OrderJpaRepository;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req.CreateOrderRequest;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req.UpdateOrderRequest;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.CreateOrderResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderedProductResponse;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import com.programmers.smrtstore.util.DateTimeUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final UserJpaRepository userJpaRepository;
    private final OrderJpaRepository orderJpaRepository;

    @Transactional
    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        return null;
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderJpaRepository.findByIdWithOrderSheetAndUser(orderId)
            .orElseThrow(() -> new OrderException(ORDER_NOT_FOUND, String.valueOf(orderId)));
        return OrderResponse.from(order);
    }

    @Transactional
    @Override
    public Long cancelOrder(Long orderId) {
        return null;
    }

    @Transactional
    @Override
    public Long updateOrder(Long orderId, UpdateOrderRequest request) {
        return null;
    }

    @Override
    public Integer calculateUserMonthlyTotalSpending(Long userId, int month, int year) {
        checkUserExistence(userId);
        DateTimeUtils.validateMonth(month);
        DateTimeUtils.validateYear(year);
        return orderJpaRepository.calculateMonthlyTotalSpending(userId, month, year);
    }

    @Override
    public Integer getTotalPriceByOrderId(Long orderId) {
        Order order = orderJpaRepository.findByIdIncludeDeleted(orderId)
            .orElseThrow(() -> new OrderException(ORDER_NOT_FOUND, String.valueOf(orderId)));
        return order.getTotalPrice();
    }

    @Override
    public List<OrderedProductResponse> getProductsForOrder(Long orderId) {
        return orderJpaRepository.findByIdWithOrderSheetIncludeDeleted(orderId)
            .orElseThrow(() -> new OrderException(ORDER_NOT_FOUND, String.valueOf(orderId)))
            .getOrderSheet().getOrderedProducts().stream()
            .map(OrderedProductResponse::from)
            .toList();
    }

    private User checkUserExistence(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }
}
