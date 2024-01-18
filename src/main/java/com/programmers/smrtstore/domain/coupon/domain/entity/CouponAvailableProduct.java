package com.programmers.smrtstore.domain.coupon.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.coupon.domain.entity.enums.CouponType;
import com.programmers.smrtstore.domain.coupon.domain.exception.CouponException;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private CouponAvailableProduct(Coupon coupon, Product product) {
        this.coupon = coupon;
        this.product = product;
    }

    public static CouponAvailableProduct of(Coupon coupon, Product product) {
        validateCouponMinPrice(coupon,product);
        validateCouponType(coupon);
        return new CouponAvailableProduct(coupon, product);
    }

    private static void validateCouponMinPrice(Coupon coupon,Product product) {
        if (coupon.getCouponValue().getMinOrderPrice() > product.getPrice()) {
            throw new CouponException(ErrorCode.COUPON_PRICE_NOT_ENOUGH);
        }
    }

    private static void validateCouponType(Coupon coupon) {
        if (!coupon.getCouponType().equals(CouponType.PRODUCT)) {
            throw new CouponException(ErrorCode.COUPON_ONLY_APPLIED_PRODUCT);
        }
    }

}
