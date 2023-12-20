package com.programmers.smrtstore.domain.order.domain.entity;

import com.programmers.smrtstore.domain.order.domain.entity.enums.OrderStatus;
import com.programmers.smrtstore.domain.order.domain.entity.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_TB")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Long userId;

    private Long shippingInfoId;

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

    private PaymentMethod paymentMethod;

    private Integer totalPrice;

}
