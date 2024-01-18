package com.programmers.smrtstore.domain.orderManagement.order.presentation.dto.res;

import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.Order;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.vo.PaymentInfo;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {

    private String id;

    private OrderStatus orderStatus;

    private Integer totalPrice;

    private String orderDate;

//    private PaymentInfo paymentInfo;

    private String deliveryInfo;

    private List<OrderedProductResponse> products;

    private ProfileUserResponse orderer;

    @Builder
    private OrderResponse(
        String id, OrderStatus orderStatus, Integer totalPrice, String orderDate,
        String deliveryInfo, List<OrderedProductResponse> products,
        ProfileUserResponse orderer
    ) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
//        this.paymentInfo = paymentInfo;
        this.deliveryInfo = deliveryInfo;
        this.products = products;
        this.orderer = orderer;
    }

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .orderStatus(order.getOrderStatus())
            .totalPrice(order.getTotalPrice())
            .orderDate(order.getOrderDate().toString())
//            .paymentInfo(order.getPaymentInfo())
            .deliveryInfo(order.getDeliveryInfo().toString())
            .products(order.getOrderSheet().getOrderedProducts().stream()
                .map(OrderedProductResponse::from)
                .toList())
            .orderer(ProfileUserResponse.from(order.getOrderSheet().getUser()))
            .build();
    }
}
