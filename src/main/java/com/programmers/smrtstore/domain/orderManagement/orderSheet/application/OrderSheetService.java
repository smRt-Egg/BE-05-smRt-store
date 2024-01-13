package com.programmers.smrtstore.domain.orderManagement.orderSheet.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.INVALID_USER;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERSHEET_ALREADY_ORDERED;
import static com.programmers.smrtstore.core.properties.ErrorCode.ORDERSHEET_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.OrderSheet;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.exception.OrderSheetException;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.infrastructure.OrderSheetJpaRepository;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.CreateOrderSheetRequest;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.res.OrderSheetDetailViewResponse;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.res.OrderSheetOrdererInfo;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.OrderSheetCouponInfo;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.OrderSheetProductInfo;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.OrderSheetUserPointInfo;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.application.OrderedProductService;
import com.programmers.smrtstore.domain.orderManagement.orderedProduct.domain.entity.OrderedProduct;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.point.application.dto.res.OrderExpectedPointDto;
import com.programmers.smrtstore.domain.user.application.service.ShippingAddressService;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderSheetService {

    private final UserJpaRepository userJpaRepository;
    private final OrderSheetJpaRepository orderSheetJpaRepository;
    private final OrderedProductService orderedProductService;
    // TODO: 의존성 작게 줄이기
    private final PointService pointService;
    private final ShippingAddressService deliveryAddressService;

    // 주문서 생성
    @Transactional
    public Long createOrderSheet(Long tokenUserId, CreateOrderSheetRequest request) {
        // 토큰 정보와 request 유저 id 일치하는지 검증
        if (!tokenUserId.equals(request.getOrdererId())) {
            throw new OrderSheetException(INVALID_USER, String.valueOf(tokenUserId));
        }

        // 유저 존재 여부
        checkUserExistence(request.getOrdererId());

        // orderedProduct 생성
        List<OrderedProduct> orderedProducts = request.getOrderedProducts().stream()
            .map(orderedProductService::createOrderedProduct)
            .toList();

        // 배송 관련 유효성 검증
        request.getDeliveryMethod().validateDeliveryFee(request.getDeliveryFee());

        // orderSheet 생성
        OrderSheet orderSheet = OrderSheet.builder()
            .user(checkUserExistence(request.getOrdererId()))
            .orderedProducts(orderedProducts)
            .build();
        orderSheetJpaRepository.save(orderSheet);

        return orderSheet.getId();
    }

    public OrderSheetDetailViewResponse getOrderSheetDetailViewById(
        Long tokenUserId, Long orderSheetId
    ) {
        User user = checkUserExistence(tokenUserId);

        OrderSheet orderSheet = orderSheetJpaRepository.getOrderSheetWithOrderedProductsById(
            orderSheetId).orElseThrow(
            () -> new OrderSheetException(ORDERSHEET_NOT_FOUND, String.valueOf(orderSheetId)));

        // 유효성 검증
        // TODO: exception 발생을 도메인 내부에서 할지 고민
        // 이미 주문이 된 주문서인지 검증
        if (orderSheet.isAvailableOrder()) {
            throw new OrderSheetException(ORDERSHEET_ALREADY_ORDERED, String.valueOf(orderSheetId));
        }
        // 주문서의 유저와 토큰의 유저가 일치하는지 검증
        orderSheet.validateOwnerOfOrderSheet(user);
        // 주문서의 orderedProducts 유효성 검증
        orderSheet.getOrderedProducts().forEach(orderedProductService::validateOrderedProduct);

        // orderSheetResponse 생성에 필요한 정보 가져오기
        // 배송지 정보
        DeliveryAddressBook deliveryAddressBook = deliveryAddressService.getShippingAddressList(
            tokenUserId);
        // 주문자 정보
        OrderSheetOrdererInfo ordererInfo = OrderSheetOrdererInfo.of(
            user.getNickName(), user.getPhone(), user.getEmail()
        );
        // 주문 상품 목록
        List<OrderSheetProductInfo> orderSheetProductInfos = orderSheet.getOrderedProducts()
            .stream()
            .map(OrderSheetProductInfo::from)
            .toList();
        // 상품에 대한 쿠폰 정보
        // TODO: 쿠폰 완성 후 고치기
        OrderSheetCouponInfo couponInfo = new OrderSheetCouponInfo();
        // 유저 포인트 정보
        OrderSheetUserPointInfo userPoint = new OrderSheetUserPointInfo(user.getPoint());
        // 예상 포인트 정보
        // TODO: 쿠폰 완성 후 null 고치기
        List<Integer> orderedProductPriceAfterCoupon = null;
        OrderExpectedPointDto orderExpectedPoint = pointService.calculateEstimatedAcmPoint(
            orderedProductPriceAfterCoupon, user.getId(), user.getMembershipYn()
        );

        return OrderSheetDetailViewResponse.builder()
            .orderSheetId(orderSheet.getId())
            .deliveryAddressBook(deliveryAddressBook)
            .ordererInfo(ordererInfo)
            .productInfo(orderSheetProductInfos)
            .couponInfo(couponInfo)
            .userPoint(userPoint)
            .rewardPoint(orderExpectedPoint)
            .build();
    }

    public void deleteOrderSheetById(Long orderSheetId) {

    }

    private User checkUserExistence(Long userId) {
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }

}

