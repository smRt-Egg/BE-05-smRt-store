package com.programmers.smrtstore.domain.point.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.orderManagement.order.application.OrderService;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderedProductResponse;
import com.programmers.smrtstore.domain.point.application.dto.req.PointHistoryRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.PointRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.UsePointRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.ReviewPointRequest;
import com.programmers.smrtstore.domain.point.application.dto.res.OrderExpectedPointDto;
import com.programmers.smrtstore.domain.point.application.dto.res.PointDetailResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.PointResponse;
import com.programmers.smrtstore.domain.point.application.dto.res.ProductEstimatedPointDto;
import com.programmers.smrtstore.domain.point.domain.entity.Point;
import com.programmers.smrtstore.domain.point.domain.entity.enums.PointStatus;
import com.programmers.smrtstore.domain.point.domain.entity.vo.TradeDateRange;
import com.programmers.smrtstore.domain.point.infrastructure.PointJpaRepository;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.review.infrastructure.ReviewJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

    private final PointFacade pointFacade;
    private final OrderService orderService;
    private final ReviewJpaRepository reviewRepository;
    private final ProductJpaRepository productRepository;
    private final UserJpaRepository userJpaRepository;
    private final PointJpaRepository pointRepository;

    public static final int MAX_AVAILALBE_USE_POINT = 2000000;
    public static final int REVIEW_POINT = 50;
    private static final int MAX_AVAILABLE_POINT = 20000;
    private static final int MAX_PRICE_FOR_FOUR = 200000; // 4% 추가 적립이 가능한 월별 쇼핑 금액 기준
    private static final int MAX_PRICE_FOR_ONE = 3000000; // 1% 추가 적립이 가능한 월별 쇼핑 금액 기준

    public Long accumulatePoint(PointRequest request) {

        Long userId = request.getUserId();
        Long orderId = request.getOrderId();

        User user = validateUserExists(userId);

        // 사용자의 월별 쇼핑 금액
        int userMonthlyTotalSpending = getUserMonthlyTotalSpending(userId);
        boolean isMembershipYn = user.getMembershipYn();

        Long pointId = null;

        // 주문상품 리스트와 상품별 결제가격 조회
        List<OrderedProductResponse> orderedProducts = orderService.getProductsForOrder(orderId);
        for (OrderedProductResponse product : orderedProducts) {
            // 상품별 (1개당) 실제 적립되는 적립금 (기본 1% 적립 + 추가 4% 적립)
            int totalAcmPoint = calculateAcmPointPerProduct(product, userMonthlyTotalSpending);
            Long id = saveAcmPointPerProduct(product, totalAcmPoint, isMembershipYn, request);
            userMonthlyTotalSpending += product.getTotalPrice();
            if (pointId == null) {
                pointId = id;
            }
        }
        return pointId;
    }

    private Long saveAcmPointPerProduct(OrderedProductResponse product,
        int totalAcmPointPerProduct, boolean isMembershipYn, PointRequest request) {

        Long pointId = null;
        int count = product.getQuantity();
        while (count != 0) {
            Point point = request.toEntity(
                PointStatus.ACCUMULATED,
                totalAcmPointPerProduct / product.getQuantity(),
                isMembershipYn
            );
            pointRepository.save(point);
            count -= 1;
            if (pointId == null) {
                pointId = point.getId();
            }
        }
        return pointId;
    }

    public OrderExpectedPointDto calculateEstimatedAcmPoint(
        List<Integer> productPrices, Long userId, boolean membershipYn
    ) {

        int totalPrice = productPrices.stream()
            .reduce(0, Integer::sum);

        int defaultPoint = totalPrice / 100; // 기본 1% 예상 적립금액
        int additionalPoint = 0;
        if (membershipYn) {
            additionalPoint = calculateEstimatedAdditionalAcmPoint(productPrices, userId);
        }
        return OrderExpectedPointDto.of(
            defaultPoint,
            additionalPoint,
            defaultPoint + additionalPoint
        );
    }

    public OrderExpectedPointDto calculateAcmPoint(Long orderId, User user) {

        // 전체 주문금액에 대한 기본 1% 적립 (=기본직립)
        int defaultPoint = calculateDefaultPoint(orderId);

        int additionalPoint = 0;
        if (user.getMembershipYn()) {
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

    public Integer calculateMaximumPointForUnwrittenReview(Long userId) {

        validateUserExists(userId);
        Long unWritteReviewCount = reviewRepository.getUnWrittenReviewCount(userId);
        return calculateTotalReviewPoint(unWritteReviewCount);
    }

    private Integer calculateTotalReviewPoint(Long unWritteReviewCount) {
        return unWritteReviewCount == 0 ? 0 : unWritteReviewCount.intValue() * REVIEW_POINT;
    }

    public Long accumulatePointByReview(ReviewPointRequest request) {

        Long userId = request.getUserId();
        User user = validateUserExists(userId);

        Point point = request.toEntity(user.getMembershipYn());
        pointRepository.save(point);
        return point.getId();
    }

    private int calculateDefaultPoint(Long orderId) {
        return orderService.getTotalPriceByOrderId(orderId) / 100;
    }

    public ProductEstimatedPointDto calculateEstimatedAcmPoint(Long productId, Long userId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        User user = validateUserExists(userId);

        // 상품 원가에 대한 기본 1% 적립 (=기본적립)
        int defaultPoint = product.getPrice() / 100;
        int additionalPoint = 0;
        if (user.getMembershipYn()) {
            // 멤버십, 월별 쇼핑 금액이 반영된 추가 멤버십 적용 금액
            additionalPoint = calculateAdditionalAcmPoint(product, userId);
        }
        return ProductEstimatedPointDto.of(
            defaultPoint,
            additionalPoint,
            defaultPoint + additionalPoint // 멤버십 적용된 최종 적립 (=구매적립)
        );
    }

    public ProductEstimatedPointDto calculateEstimatedAcmPointWithoutUserId(Long productId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        int defaultPoint = product.getPrice() / 100;
        int additionalPoint = calculateAdditionalAcmPoint(product, null);
        return ProductEstimatedPointDto.of(
            defaultPoint,
            additionalPoint,
            defaultPoint + additionalPoint
        );
    }

    private int calculateAdditionalAcmPoint(Product product, Long userId) {

        if (userId != null) {
            int userMonthlyTotalSpending = getUserMonthlyTotalSpending(userId);
            return calculateAdditionalPointPerProduct(product.getPrice(), userMonthlyTotalSpending);
        }
        return calculateAdditionalPointPerProduct(product.getPrice(), 0);
    }

    private int calculateAcmPointPerProduct(OrderedProductResponse product, int userMonthlyTotalSpending) {

        int defaultPoint = product.getTotalPrice() / 100; // 상품별 기본 1% 적립금
        int additionalPoint = calculateAdditionalAcmPoint(product, userMonthlyTotalSpending); // 상품별 추가 4% 적립금
        return (defaultPoint + additionalPoint) / product.getQuantity(); // 구매한 상품 1개에 대한 최종 적립금
    }

    private int calculateAdditionalAcmPoint(OrderedProductResponse product, int userMonthlyTotalSpending) {
        return calculateAdditionalPointPerProduct(product.getTotalPrice(), userMonthlyTotalSpending);
    }

    private int calculateAdditionalAcmPoint(List<OrderedProductResponse> orderedProducts, Long userId) {

        int userMonthlyTotalSpending = getUserMonthlyTotalSpending(userId);
        return calculateAdditionalPoint(orderedProducts, userMonthlyTotalSpending);
    }

    private int calculateEstimatedAdditionalAcmPoint(List<Integer> productPrices, Long userId) {

        int userMonthlyTotalSpending = getUserMonthlyTotalSpending(userId);
        return calculateEstimatedAdditionalPoint(productPrices, userMonthlyTotalSpending);
    }

    private int getUserMonthlyTotalSpending(Long userId) {

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        return orderService.calculateUserMonthlyTotalSpending(userId, month, year);
    }

    private int calculateAdditionalPoint(List<OrderedProductResponse> orderedProducts, int userMonthlyTotalSpending) {

        int point = 0;
        for (OrderedProductResponse productResponse : orderedProducts) {
            int totalPricePerProduct = productResponse.getTotalPrice();
            point += calculateAdditionalPointPerProduct(totalPricePerProduct, userMonthlyTotalSpending);
            userMonthlyTotalSpending += totalPricePerProduct;
        }
        return point;
    }

    private int calculateEstimatedAdditionalPoint(List<Integer> productPrices, int userMonthlytotalSpending) {

        int point = 0;
        for (Integer price : productPrices) {
            point += calculateAdditionalPointPerProduct(price, userMonthlytotalSpending);
            userMonthlytotalSpending += price;
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
        }
        return available * rate / 100 + (totalPrice - available) * nextRate / 100;
    }

    private boolean isHigherThanMaxAvailablePoint(int pointAmount) {
        return pointAmount >= MAX_AVAILABLE_POINT;
    }


    public Long usePoint(UsePointRequest request) {

        User user = validateUserExists(request.getUserId());

        Point point = request.toEntity(user.getMembershipYn());
        pointRepository.save(point);
        return point.getId();
    }

    public Long cancelUsedPoint(PointRequest request) {

        validateUserExists(request.getUserId());

        Long orderId = request.getOrderId();

        // 차감된 포인트 이력 가져오기
        PointResponse pointResponse = pointFacade.getUsedPointByOrderId(orderId);

        Point point = request.toEntity(
            PointStatus.USE_CANCELED,
            // 음수 값에 대해 음수 처리 -> 양수
            pointFacade.makeNegativeNumber(pointResponse.getPointValue()),
            pointResponse.getMembershipApplyYn()
        );
        pointRepository.save(point);

        int expiredPoint = calculateExpiredPoint(pointResponse.getOrderId());
        if (expiredPoint != 0) {
            Point expiredpoint = request.toEntity(
                PointStatus.EXPIRED,
                expiredPoint,
                pointResponse.getMembershipApplyYn());
            pointRepository.save(expiredpoint);
        }
        return point.getId();
    }

    private int calculateExpiredPoint(Long orderId) {

        List<PointDetailResponse> usedDetailHistory = pointFacade.getUsedDetailByOrderId(orderId);

        int expiredPoint = 0;
        for (PointDetailResponse pointDetail : usedDetailHistory) {
            PointResponse originAcmPoint = pointFacade.getPointById(pointDetail.getPointId());
            if (pointFacade.validateExpiredAt(originAcmPoint)) {
                expiredPoint += pointDetail.getPointValue();
            }
        }
        return expiredPoint;
    }

    public List<PointResponse> getPointHistory(PointHistoryRequest request) {

        Long userId = request.getUserId();
        PointStatus pointStatus = request.getPointStatus();
        TradeDateRange tradeDateRange = request.getTradeDateRange();

        return pointFacade.getPointHistoryByIssuedAtAndStatus(userId, pointStatus, tradeDateRange);
    }

    private User validateUserExists(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND, String.valueOf(userId)));
    }
}
