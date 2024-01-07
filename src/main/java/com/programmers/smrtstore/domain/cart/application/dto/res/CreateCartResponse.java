package com.programmers.smrtstore.domain.cart.application.dto.res;

import com.programmers.smrtstore.domain.cart.domain.entity.Cart;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateCartResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private Long productDetailOptionId;
    private Integer quantity;
    private Integer price;
    private LocalDateTime createdAt;


    public static CreateCartResponse from(Cart cart) {
        return new CreateCartResponse(
            cart.getId(),
            cart.getUser().getId(),
            cart.getProduct().getId(),
            cart.getProductDetailOptionId(),
            cart.getQuantity(),
            cart.getPrice(),
            cart.getCreatedAt()
        );
    }
}
