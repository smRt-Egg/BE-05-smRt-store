package com.programmers.smrtstore.domain.cart.presentation.dto.res;

import com.programmers.smrtstore.domain.cart.domain.entity.Cart;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private Long productDetailOptionId;
    private Integer quantity;
    private Integer price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CartResponse from(Cart cart) {
        return new CartResponse(
            cart.getId(),
            cart.getUser().getId(),
            cart.getProduct().getId(),
            cart.getProductDetailOptionId(),
            cart.getQuantity(),
            cart.getPrice(),
            cart.getCreatedAt(),
            cart.getUpdatedAt()
        );
    }
}
