package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "coupon_available_product_TB")
public class CouponAvailableProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private CouponAvailableProduct(Coupon coupon, Product product) {
        this.coupon = coupon;
        this.product = product;
    }

    public static CouponAvailableProduct of(Coupon coupon, Product product) {
        return new CouponAvailableProduct(coupon, product);
    }

}
