package com.programmers.smrtstore.domain.orderManagement.order.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERSHEET_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDER_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.SHIPPING_ADDRESS_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;

import com.programmers.smrtstore.domain.coupon.application.OrderCouponService;
import com.programmers.smrtstore.domain.orderManagement.delivery.entity.DeliveryInfo;
import com.programmers.smrtstore.domain.orderManagement.delivery.infrastructure.DeliveryInfoJpaRepository;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus;
import com.programmers.smrtstore.domain.orderManagement.order.exception.OrderException;
import com.programmers.smrtstore.domain.orderManagement.order.infrastructure.OrderJpaRepository;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req.CreateOrderRequest;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.req.UpdateOrderRequest;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderPreviewResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderResponse;
import com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res.OrderedProductResponse;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.application.OrderSheetService;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.OrderSheet;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.infrastructure.OrderSheetJpaRepository;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.CouponApplyResult;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import com.programmers.smrtstore.domain.point.application.PointDetailService;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.point.application.dto.req.PointDetailRequest;
import com.programmers.smrtstore.domain.point.application.dto.req.UsePointRequest;
import com.programmers.smrtstore.domain.user.domain.entity.ShippingAddress;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.ShippingAddressJpaRepository;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import com.programmers.smrtstore.util.DateTimeUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final UserJpaRepository userJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderSheetJpaRepository orderSheetJpaRepository;
    private final ShippingAddressJpaRepository shippingAddressJpaRepository;
    private final DeliveryInfoJpaRepository deliveryInfoJpaRepository;
    //  TODO: validateOrderSheet() 메소드 도메인 내부로 분리
    private final OrderSheetService orderSheetService;
    private final PointDetailService pointDetailService;
    private final PointService pointService;

    private final OrderCouponService orderCouponService;

    @Transactional
    @Override
    public String createOrder(Long userId, CreateOrderRequest request) {
        User user = checkUserExistence(userId);

        OrderSheet orderSheet = orderSheetJpaRepository.findById(request.getOrderSheetId())
            .orElseThrow(() -> new OrderException(ORDERSHEET_NOT_FOUND,
                String.valueOf(request.getOrderSheetId())));

        // 주문서 유효성 검증
        orderSheetService.validateAvailableOrderSheet(orderSheet, user);

        List<OrderedProduct> orderedProducts = orderSheet.getOrderedProducts();

        // 최종적으로 적용된 쿠폰에 대해 쿠폰 적용 결과 산출
        Map<Long, List<CouponApplyResult>> finalCouponAppliedResult = orderCouponService.calCouponApplyResult(
            orderedProducts, request.getSelectedCoupons());
        orderedProducts.forEach(orderedProduct -> orderedProduct.updateCouponDiscount(
            finalCouponAppliedResult.get(orderedProduct.getId()).stream()
                .map(CouponApplyResult::getDiscountAmount).reduce(0, Integer::sum))
        );

        // 최종적으로 포인트 사용액에 대해 포인트 적용 결과 산출
        Map<Long, Integer> finalPointUsedResult = pointDetailService.getPointPiecesByOrderedProduct(
            orderedProducts, request.getUsedPoint());
        orderedProducts.forEach(orderedProduct -> orderedProduct.updatePointDiscount(
            finalPointUsedResult.get(orderedProduct.getId()))
        );

        // 배송비 쿠폰 적용
        if (request.getSelectedCoupons().getSelectedDeliveryFeeCouponId() != null) {
            orderSheet.getDeliveryOptions().useDeliveryFeeCoupon();
        }

        // delivery entity 생성
        ShippingAddress shippingAddress = shippingAddressJpaRepository.findById(
                request.getDeliveryAddress().getShippingAddressId())
            .orElseThrow(() -> new OrderException(SHIPPING_ADDRESS_NOT_FOUND,
                String.valueOf(request.getDeliveryAddress().getShippingAddressId())));

        DeliveryInfo deliveryInfo = request.getDeliveryAddress()
            .createDeliveryInfoFrom(shippingAddress);
        deliveryInfoJpaRepository.save(deliveryInfo);

        // order 생성
        // 주문서 상태 변경 (order 넣기)
        // TODO: 동시성 고려해서 id 생성
        Order order = Order.builder()
            .id(SimpleIdGenerator.generateId())
            .orderSheet(orderSheet)
            .orderDate(LocalDateTime.now())
            .deliveryInfo(deliveryInfo)
            .build();
        orderJpaRepository.save(order);

        // TODO: 쿠폰 적용 트랜잭션

        // 포인트 적용 트랜잭션
        Long pointId = pointService.usePoint(UsePointRequest.builder()
            .userId(userId)
            .orderId(orderSheet.getOrder().getId())
            .pointAmount(request.getUsedPoint())
            .build());
        pointDetailService.saveUseHistory(PointDetailRequest.builder()
            .userId(userId)
            .pointId(pointId)
            .build());

        return order.getId();
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
    public Integer getTotalPriceByOrderId(String orderId) {
        Order order = orderJpaRepository.findByIdIncludeDeleted(orderId)
            .orElseThrow(() -> new OrderException(ORDER_NOT_FOUND, String.valueOf(orderId)));
        return order.getTotalPrice();
    }

    @Override
    public List<OrderedProductResponse> getProductsForOrder(String orderId) {
        return orderJpaRepository.findByIdWithOrderSheetIncludeDeleted(orderId)
            .orElseThrow(() -> new OrderException(ORDER_NOT_FOUND, String.valueOf(orderId)))
            .getOrderSheet().getOrderedProducts().stream()
            .map(OrderedProductResponse::from)
            .toList();
    }

    @Override
    public List<OrderPreviewResponse> getOrderPreviewsByUserId(Long userId) {
        checkUserExistence(userId);
        List<Order> orders = orderJpaRepository.findByUserId(userId);

        return orders.stream()
            .map(OrderPreviewResponse::from)
            .toList();
    }

    @Override
    public List<OrderPreviewResponse> getOrderPreviewsByUserIdAndStatus(
        Long userId, List<OrderStatus> statuses
    ) {
        checkUserExistence(userId);
        return orderJpaRepository.findOrdersByStatusesAndUserId(userId, statuses).stream()
            .map(OrderPreviewResponse::from)
            .toList();
    }

    @Override
    public Long getActiveOrderCountByUserId(Long userId) {
        checkUserExistence(userId);
        return orderJpaRepository.findOrderCountByStatusesAndUserId(userId, OrderStatus.getActiveOrderStatus());
    }

    private User checkUserExistence(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }
}
