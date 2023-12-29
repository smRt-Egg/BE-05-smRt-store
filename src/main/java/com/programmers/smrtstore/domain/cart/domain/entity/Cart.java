package com.programmers.smrtstore.domain.cart.domain.entity;

import com.programmers.smrtstore.core.base.TimestampBaseEntity;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart_TB")
@Entity
public class Cart extends TimestampBaseEntity {

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CartProduct> cartProducts = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Builder
    private Cart(User user) {
        this.user = user;
        this.quantity = 0;
        this.totalPrice = 0;
    }

    public void addCartProduct(CartProduct cartProduct) {
        cartProducts.add(cartProduct);
        this.quantity = calculateQuantity();
        this.totalPrice = calculateTotalPrice();
    }

    public void removeCartProduct(CartProduct cartProduct) {
        cartProducts.remove(cartProduct);
        this.quantity = calculateQuantity();
        this.totalPrice = calculateTotalPrice();
    }

    private Integer calculateQuantity() {
        return cartProducts.stream().map(CartProduct::getQuantity).reduce(0, Integer::sum);
    }

    private Integer calculateTotalPrice() {
        return cartProducts.stream().map(CartProduct::getPrice).reduce(0, Integer::sum);
    }
}
