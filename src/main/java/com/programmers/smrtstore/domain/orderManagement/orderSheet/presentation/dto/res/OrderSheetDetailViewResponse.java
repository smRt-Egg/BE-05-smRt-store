package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.res;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.vo.DeliveryOptions;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.OrderSheetCouponInfo;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.OrderSheetProductInfo;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo.OrderSheetUserPointInfo;
import com.programmers.smrtstore.domain.point.application.dto.res.OrderExpectedPointDto;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import java.util.List;
import lombok.Builder;

public class OrderSheetDetailViewResponse {

    private Long orderSheetId;
    // 유저 배송지 목록
    private DeliveryAddressBook deliveryAddressBook;
    // 주문자 정보
    private OrderSheetOrdererInfo ordererInfo;
    // 주문 상품 목록 (즉시 할인 + 추가할인 + 총 주문금액)
    private List<OrderSheetProductInfo> productInfo;
    // 상품에 대한 쿠폰 정보
    private OrderSheetCouponInfo couponInfo;
    // 유저 포인트 정보
    private OrderSheetUserPointInfo userPoint;
    // TODO: 결제 도메인을 만든 후
    // 결제 수단
    // private OrderSheetPayMethod payMethod;
    // TODO: 포인트 혜택 따로
    private OrderExpectedPointDto rewardPoint;
    // 배송비
    private DeliveryOptions deliveryOptions;

    @Builder
    public OrderSheetDetailViewResponse(
        Long orderSheetId, DeliveryAddressBook deliveryAddressBook,
        OrderSheetOrdererInfo ordererInfo,
        List<OrderSheetProductInfo> productInfo, OrderSheetCouponInfo couponInfo,
        OrderSheetUserPointInfo userPoint,
//        OrderSheetPayMethod payMethod,
        OrderExpectedPointDto rewardPoint, DeliveryOptions deliveryOptions
    ) {
        this.orderSheetId = orderSheetId;
        this.deliveryAddressBook = deliveryAddressBook;
        this.ordererInfo = ordererInfo;
        this.productInfo = productInfo;
        this.couponInfo = couponInfo;
        this.userPoint = userPoint;
//        this.payMethod = payMethod;
        this.rewardPoint = rewardPoint;
        this.deliveryOptions = deliveryOptions;
    }
}
