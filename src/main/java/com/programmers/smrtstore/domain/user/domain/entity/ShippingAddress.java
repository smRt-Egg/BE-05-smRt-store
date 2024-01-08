package com.programmers.smrtstore.domain.user.domain.entity;

import com.programmers.smrtstore.domain.user.presentation.dto.req.DetailShippingRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "shipping_address_TB")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10)
    private String name;

    @Column(nullable = false, length = 10)
    private String recipient;

    @Column(nullable = false, length = 50)
    private String address1Depth;

    @Column(nullable = false, length = 30)
    private String address2Depth;

    @Column(nullable = false, length = 10)
    private String zipCode;

    @Column(nullable = false, length = 15)
    private String phoneNum1;

    @Column(length = 15)
    private String phoneNum2;

    @Column(nullable = false)
    private Boolean defaultYn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void disableDefault() {
        defaultYn = false;
    }

    public void updateShippingAddress(DetailShippingRequest request) {
        this.name = request.getName();
        this.recipient = request.getRecipient();
        this.address1Depth = request.getAddress1Depth();
        this.address2Depth = request.getAddress2Depth();
        this.zipCode = request.getZipCode();
        this.phoneNum1 = request.getPhoneNum1();
        this.phoneNum2 = request.getPhoneNum2();
        this.defaultYn = request.getDefaultYn();
    }
}
