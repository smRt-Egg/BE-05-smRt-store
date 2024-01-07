package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.order.application.OrderService;
import com.programmers.smrtstore.domain.order.presentation.dto.res.OrderedProductResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.OrderExpectedPointDto;
import com.programmers.smrtstore.domain.point.application.dto.res.ProductEstimatedPointDto;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.exception.PointException;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.point.application.dto.req.PointRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.UsePointRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
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
    private final ProductJpaRepository productRepository;
    private final UserRepository userRepository;
    private final PointJpaRepository pointRepository;
  
    public static final int MAX_AVAILALBE_USE_POINT = 2000000;
    private static final int MAX_AVAILABLE_POINT = 20000;
    private static final int MAX_PRICE_FOR_FOUR = 200000; // 4% 추가 적립이 가능한 월별 쇼핑 금액 기준
    private static final int MAX_PRICE_FOR_ONE = 3000000; // 1% 추가 적립이 가능한 월별 쇼핑 금액 기준

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

        OrderExpectedPointDto expectedPoint = calculateAcmPoint(orderId, user);
        Point point = request.toEntity(PointStatus.ACCUMULATED, expectedPoint.getTotalPoint(), user.isMembershipYn());
        pointRepository.save(point);
        return PointResponse.from(point);
    }

    public OrderExpectedPointDto calculateAcmPoint(Long orderId, User user) {

        // 전체 주문금액에 대한 기본 1% 적립 (=기본직립)
        int defaultPoint = calculateDefaultPoint(orderId);

        int additionalPoint = 0;
        if (user.isMembershipYn()) {
            List<OrderedProductResponse> orderedProducts = orderService.getProductsForOrder(orderId);
            // 멤버십, 월별 쇼핑 금액이 반영된 추가 멤버십 적용 금액
            additionalPoint = calculateAdditionalAcmPoint(orderedProducts, user.getId());
        }
        return OrderExpectedPointDto.of(
            defaultPoint,
            additionalPoint,
            defaultPoint + additionalPoint // 멤버십 적용된 최종 적립 (=구매적립)
        );
    }

    public ProductEstimatedPointDto calculateEstimatedAcmPoint(Long productId, User user) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        // 상품 원가에 대한 기본 1% 적립 (=기본적립)
        int defaultPoint = product.getPrice() / 100;
        int additionalPoint = 0;
        if (user.isMembershipYn()) {
            // 멤버십, 월별 쇼핑 금액이 반영된 추가 멤버십 적용 금액
            additionalPoint = calculateAdditionalAcmPoint(product, user.getId());
        }
        return ProductEstimatedPointDto.of(
            defaultPoint,
            additionalPoint,
            defaultPoint + additionalPoint // 멤버십 적용된 최종 적립 (=구매적립)
        );
    }

    public int calculateDefaultPoint(Long orderId) {
        return orderService.getTotalPriceByOrderId(orderId) / 100;
    }

    public int calculateAdditionalAcmPoint(List<OrderedProductResponse> orderedProducts, Long userId) {

        int userMonthlyTotalSpending = getUserMonthlyTotalSpeding(userId);
        return calculateAdditionalPoint(orderedProducts, userMonthlyTotalSpending);
    }

    public int calculateAdditionalAcmPoint(Product product, Long userId) {

        int userMonthlyTotalSpending = getUserMonthlyTotalSpeding(userId);
        return calculateAdditionalPointPerProduct(product.getPrice(), userMonthlyTotalSpending);
    }

    private int getUserMonthlyTotalSpeding(Long userId) {

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        return orderService.calculateUserMonthlyTotalSpending(userId, month, year);
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

    private PointResponse getByOrderIdAndStatus(Long orderId, PointStatus pointStatus) {
        return pointRepository.findByOrderIdAndPointStatus(orderId, pointStatus)
            .orElseThrow(() -> new PointException(ErrorCode.POINT_NOT_FOUND));
    }

    public PointResponse cancelAccumulatedPoint(PointRequest request) {

        validateUserExists(request.getUserId());

        Long orderId = request.getOrderId();
        PointResponse pointResponse = getByOrderIdAndStatus(orderId, PointStatus.ACCUMULATED);

        Point point = request.toEntity(
            PointStatus.ACCUMULATE_CANCELED,
            pointResponse.getPointValue() * -1,
            pointResponse.getMembershipApplyYn());
        pointRepository.save(point);
        return PointResponse.from(point);
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
