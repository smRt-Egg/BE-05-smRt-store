package com.programmers.smrtstore.domain.cart.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart_product_TB")
@Entity
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Integer price;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder
    private CartProduct(Product product, Integer quantity, Cart cart) {
        this.product = product;
        this.quantity = quantity;
        this.price = calculatePrice(product, quantity);
        this.cart = cart;
        this.cart.addCartProduct(this);
    }

    private static Integer calculatePrice(Product product, Integer quantity) {
        if (product.isDiscountYn()) {
            int salePrice = product.getSalePrice();
            return (salePrice - (int) (salePrice * product.getDiscountRatio() / 100)) * quantity;
        }
        return product.getSalePrice() * quantity;
    }

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
        this.price = calculatePrice(product, quantity);
    }
}
