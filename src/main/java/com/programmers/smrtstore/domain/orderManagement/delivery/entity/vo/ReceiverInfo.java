package com.programmers.smrtstore.domain.orderManagement.delivery.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReceiverInfo {

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_phone")
    private String receiverPhone;

    public ReceiverInfo(String receiverName, String receiverPhone) {
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
    }
}
