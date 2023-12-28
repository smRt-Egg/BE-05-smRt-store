package com.programmers.smrtstore.domain.order.domain.entity;

import com.programmers.smrtstore.domain.order.domain.entity.vo.DeliveryAddress;
import com.programmers.smrtstore.domain.order.domain.entity.vo.ReceiverInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delivery_info_TB")
@Entity
public class DeliveryInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private DeliveryAddress deliveryAddress;

    @Embedded
    private ReceiverInfo receiverInfo;

    @Column(name = "delivery_request")
    private String deliveryRequest;

    @Column(name = "delivery_fee")
    private Integer deliveryFee;

}
