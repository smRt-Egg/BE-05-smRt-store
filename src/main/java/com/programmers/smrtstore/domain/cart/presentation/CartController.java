package com.programmers.smrtstore.domain.cart.presentation;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.cart.application.CartService;
import com.programmers.smrtstore.domain.cart.application.dto.req.CreateCartRequest;
import com.programmers.smrtstore.domain.cart.application.dto.req.UpdateCartOptionRequest;
import com.programmers.smrtstore.domain.cart.application.dto.req.UpdateCartQuantityRequest;
import com.programmers.smrtstore.domain.cart.application.dto.res.CartResponse;
import com.programmers.smrtstore.domain.cart.application.dto.res.CreateCartResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CreateCartResponse> createCart(@UserId Long userId, @RequestBody CreateCartRequest request) {
        CreateCartResponse response = cartService.createCart(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCartById(@PathVariable Long cartId) {
        CartResponse response = cartService.getCartById(cartId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CartResponse>> getAllCarts(@UserId Long userId) {
        List<CartResponse> response = cartService.getAllCarts(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity<CartResponse> updateCartQuantity(@UserId Long userId, @PathVariable Long cartId, @RequestBody
    UpdateCartQuantityRequest request) {
        CartResponse response = cartService.updateCartQuantity(userId, cartId, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity<CartResponse> updateCartProductOption(@UserId Long userId, @PathVariable Long cartId, @RequestBody
    UpdateCartOptionRequest request) {
        CartResponse response = cartService.updateCartProductOption(userId, cartId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }
}
