package com.programmers.smrtstore.domain.order.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;

import com.programmers.smrtstore.domain.order.infrastructure.OrderJpaRepository;
import com.programmers.smrtstore.domain.order.presentation.dto.req.CreateOrderRequest;
import com.programmers.smrtstore.domain.order.presentation.dto.req.CreateOrderSheetRequest;
import com.programmers.smrtstore.domain.order.presentation.dto.req.UpdateOrderRequest;
import com.programmers.smrtstore.domain.order.presentation.dto.res.CreateOrderResponse;
import com.programmers.smrtstore.domain.order.presentation.dto.res.CreateOrderSheetResponse;
import com.programmers.smrtstore.domain.order.presentation.dto.res.OrderResponse;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public CreateOrderSheetResponse createOrderSheet(CreateOrderSheetRequest request) {
        return null;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        return null;
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        return null;
    }

    @Override
    public Long cancelOrder(Long orderId) {
        return null;
    }

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

    private User checkUserExistence(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }
}
