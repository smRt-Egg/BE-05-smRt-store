package com.programmers.smrtstore.domain.orderManagement.order.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.ORDER_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.orderManagement.order.exception.OrderException;
import com.programmers.smrtstore.domain.orderManagement.order.infrastructure.OrderJpaRepository;
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
public class OrderPointService {
    private final UserJpaRepository userJpaRepository;
    private final OrderJpaRepository orderJpaRepository;

    public Integer calculateUserMonthlyTotalSpending(Long userId, int month, int year) {
        checkUserExistence(userId);
        DateTimeUtils.validateMonth(month);
        DateTimeUtils.validateYear(year);
        return orderJpaRepository.calculateMonthlyTotalSpending(userId, month, year);
    }

    public Integer getTotalPriceByOrderId(String orderId) {
        Order order = orderJpaRepository.findByIdIncludeDeleted(orderId)
            .orElseThrow(() -> new OrderException(ORDER_NOT_FOUND, String.valueOf(orderId)));
        return order.getTotalPrice();
    }

    public List<OrderedProductResponse> getProductsForOrder(String orderId) {
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
