package com.programmers.smrtstore.domain.orderManagement.order.domain.entity;

import com.programmers.smrtstore.common.base.TimestampBaseEntity;
import com.programmers.smrtstore.domain.orderManagement.delivery.entity.DeliveryInfo;
import com.programmers.smrtstore.domain.orderManagement.order.domain.entity.enums.OrderStatus;
import com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.OrderSheet;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_TB")
@Entity
public class Order extends TimestampBaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @OneToOne
    @JoinColumn(name = "order_sheet_id")
    private OrderSheet orderSheet;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_info_id")
    private DeliveryInfo deliveryInfo;

//    @Embedded
//    private PaymentInfo paymentInfo;

    @Builder
    public Order(
        String id, OrderSheet orderSheet, LocalDateTime orderDate, DeliveryInfo deliveryInfo
    ) {
        this.id = id;
        this.orderSheet = orderSheet;
        orderSheet.setOrder(this);
        this.orderStatus = OrderStatus.PAYMENT_COMPLETED;
        this.totalPrice = orderSheet.getOrderSheetTotalPrice();
        this.orderDate = orderDate;
        this.deliveryInfo = deliveryInfo;
//        this.paymentInfo = paymentInfo;
    }

}
