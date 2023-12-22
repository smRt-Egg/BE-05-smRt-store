package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "Product_Coupon_TB",
        uniqueConstraints = {
        @UniqueConstraint(name = "UniqueProductAndCoupon",columnNames = {"product_id","coupon_id"})})
public class ProductCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id",nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product productId;

    @Builder
    public ProductCoupon(Coupon coupon, Product productId) {
        this.coupon = coupon;
        this.productId = productId;
    }
}
