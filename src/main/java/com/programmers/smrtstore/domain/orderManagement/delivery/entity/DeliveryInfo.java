package com.programmers.smrtstore.domain.orderManagement.delivery.entity;

import com.programmers.smrtstore.domain.orderManagement.delivery.entity.vo.DeliveryAddress;
import com.programmers.smrtstore.domain.orderManagement.delivery.entity.vo.ReceiverInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public DeliveryInfo(
        Long id, String address1Depth, String address2Depth, String zipCode,
        String receiverName, String receiverPhone, String deliveryRequest
    ) {
        this.id = id;
        this.deliveryAddress = new DeliveryAddress(address1Depth, address2Depth, zipCode);
        this.receiverInfo = new ReceiverInfo(receiverName, receiverPhone);
        this.deliveryRequest = deliveryRequest;
    }
}
