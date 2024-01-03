package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.order.application.OrderService;
import com.programmers.smrtstore.domain.order.presentation.dto.res.OrderedProductResponse;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.exception.PointException;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.req.PointRequest;
import com.programmers.smrtstore.domain.point.presentation.dto.req.UsePointRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final OrderService orderService;
    private final UserRepository userRepository;
    private final PointJpaRepository pointRepository;

    private static final int MAX_AVAILABLE_POINT = 20000;
    private static final int MAX_PRICE_FOR_FOUR = 200000;
    private static final int MAX_PRICE_FOR_ONE = 3000000;

    @Transactional(readOnly = true)
    public PointResponse getPointById(Long pointId) {
        Point point = pointRepository.findById(pointId)
            .orElseThrow(() -> new PointException(ErrorCode.POINT_NOT_FOUND));
        return PointResponse.from(point);
    }

    public PointResponse accumulatePoint(PointRequest request) {

        Long userId = request.getUserId();
        Long orderId = request.getOrderId();

        User user = validateUserExists(userId);

        int acmPoint = calculatePoint(orderId, user);
        Point point = request.toEntity(PointStatus.ACCUMULATED, acmPoint, user.isMembershipYN());
        pointRepository.save(point);
        return PointResponse.from(point);
    }

    public int calculatePoint(Long orderId, User user) {

        int defaultPoint = calculateDefaultPoint(orderId); // 전체 주문금액에 대한 기본 1% 적립 (=기본직립)

        if (user.isMembershipYN()) {
            List<OrderedProductResponse> orderedProducts = orderService.getProductsForOrder(orderId);
            return calculateExpectedAcmPoint(defaultPoint, orderedProducts, user.getId()); // 멤버십 적용된 최종 적립 (=구매적립)
        }
        return defaultPoint;
    }

    public int calculateDefaultPoint(Long orderId) {
        return orderService.getTotalPriceByOrderId(orderId) / 100;
    }

    public int calculateExpectedAcmPoint(int defaultPoint, List<OrderedProductResponse> orderedProducts, Long userId) {

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        int userMonthlyTotalSpending = orderService.calculateUserMonthlyTotalSpending(userId, month, year);
        return defaultPoint + calculateAdditionalPoint(orderedProducts, userMonthlyTotalSpending);
    }

    public int calculateAdditionalPoint(List<OrderedProductResponse> orderedProducts, int userMonthlyTotalSpending) {

        int point = 0;
        for (OrderedProductResponse productResponse : orderedProducts) {
            int totalPricePerProduct = productResponse.getTotalPrice();
            point += calculateAdditionalPointPerProduct(totalPricePerProduct, userMonthlyTotalSpending);
            userMonthlyTotalSpending += totalPricePerProduct;
        }
        return point;
    }

    private int calculateAdditionalPointPerProduct(int totalPrice, int userMonthlyTotalSpending) {
        int point = 0;
        int available = 0;

        if (MAX_PRICE_FOR_FOUR > userMonthlyTotalSpending) {
            available = MAX_PRICE_FOR_FOUR - userMonthlyTotalSpending;
            point = calculateAdditionalPointByRate(available, totalPrice, 4, 1);
        } else if (MAX_PRICE_FOR_ONE > userMonthlyTotalSpending) {
            available = MAX_PRICE_FOR_ONE - userMonthlyTotalSpending;
            point = calculateAdditionalPointByRate(available, totalPrice, 1, 0);
        }
        return isHigherThanMaxAvailablePoint(point) ? MAX_AVAILABLE_POINT : point;
    }

    private int calculateAdditionalPointByRate(int available, int totalPrice, int rate, int nextRate) {
        if (available > totalPrice) {
            return totalPrice * rate / 100;
        } else {
            return available * rate / 100 + (totalPrice - available) * nextRate / 100;
        }
    }

    private boolean isHigherThanMaxAvailablePoint(int pointAmount) {
        return pointAmount >= MAX_AVAILABLE_POINT;
    }

    private User validateUserExists(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }

    public PointResponse cancelAccumulatedPoint(PointRequest request) {
        return null;
    }

    public PointResponse usePoint(UsePointRequest request) {
        return null;
    }

    public PointResponse cancelUsedPoint(PointRequest request) {
        return null;
    }

    public PointResponse expirePoint(Long userId) {
        return null;
    }

    public PointResponse getPointHistory(Long userId) {
        return null;
    }
}
