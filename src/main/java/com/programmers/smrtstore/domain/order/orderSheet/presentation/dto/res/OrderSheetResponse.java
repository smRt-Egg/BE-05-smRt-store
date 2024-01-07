package com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.res;

import com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo.OrderSheetCouponInfo;
import com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo.OrderSheetPayMethod;
import com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo.OrderSheetProductInfo;
import com.programmers.smrtstore.domain.order.orderSheet.presentation.dto.vo.OrderSheetUserPointInfo;
import com.programmers.smrtstore.domain.point.application.dto.res.OrderExpectedPointDto;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DeliveryAddressBook;
import java.util.List;

public class OrderSheetResponse {

    private Long orderSheetId;
    // 유저 배송지 목록
    private DeliveryAddressBook deliveryAddressBook;
    // 주문자 정보
    private OrderSheetOrdererInfo ordererInfo;
    // 주문 상품 목록 (즉시 할인 + 추가할인 + 배송비 + 총 주문금액)
    private List<OrderSheetProductInfo> productInfo;
    // 상품에 대한 쿠폰 정보
    private OrderSheetCouponInfo couponInfo;
    // 유저 포인트 정보
    private OrderSheetUserPointInfo userPoint;
    // 결제 수단
    private OrderSheetPayMethod payMethod;
    // TODO: 포인트 혜택 따로
    private OrderExpectedPointDto rewardPoint;
}
