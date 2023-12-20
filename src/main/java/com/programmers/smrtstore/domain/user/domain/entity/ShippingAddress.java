package com.programmers.smrtstore.domain.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "shipping_address_TB")
@Getter
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String address1Depth;

    @Column(nullable = false, length = 30)
    private String address2Depth;

    @Column(nullable = false, length = 10)
    private String zipCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
