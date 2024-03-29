package com.programmers.smrtstore.domain.cart.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.cart.exception.CartException;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
import com.programmers.smrtstore.domain.user.domain.entity.User;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "cart_TB")
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_detail_option_id", nullable = false)
    private Long productDetailOptionId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price", nullable = false)
    private Integer price;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Cart(User user, Product product, ProductDetailOption detailOption, Integer quantity) {
        this.user = user;
        this.product = product;
        validateDetailOption(product, detailOption);
        this.productDetailOptionId = detailOption.getId();
        this.quantity = quantity;
        this.price = calculatePrice(product, detailOption.getId(), quantity);
    }

    private static final Integer DEFAULT_QUANTITY = 0;

    public static Cart of(User user, Product product, ProductDetailOption productDetailOption) {
        return new Cart(
            user, product, productDetailOption, DEFAULT_QUANTITY
        );
    }

    private static Integer calculatePrice(Product product, Long detailOptionId, Integer quantity) {
        return product.getSalePrice(detailOptionId) * quantity;
    }

    private static void validateDetailOption(Product product, ProductDetailOption detailOption) {
        if (!detailOption.getProduct().getId().equals(product.getId())) {
            throw new CartException(ErrorCode.PRODUCT_DETAIL_OPTION_NOT_MATCH);
        }
    }

    private void validateUser(Long userId) {
        if(!userId.equals(user.getId())) {
            throw new CartException(ErrorCode.CART_REQUEST_USER_MISMATCH);
        }
    }

    public void updateQuantity(Integer quantity, Long userId) {
        validateUser(userId);
        if (this.quantity + quantity < 0) {
            throw new CartException(ErrorCode.CART_QUANTITY_NOT_ENOUGH);
        }
        this.quantity += quantity;
        this.price = calculatePrice(product, productDetailOptionId, this.quantity);
    }

    public void updateDetailOption(ProductDetailOption detailOption, Long userId) {
        validateDetailOption(product, detailOption);
        validateUser(userId);
        this.productDetailOptionId = detailOption.getId();
        this.price = calculatePrice(product, detailOption.getId(), quantity);
    }

}
