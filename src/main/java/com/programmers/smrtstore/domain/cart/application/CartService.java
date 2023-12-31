package com.programmers.smrtstore.domain.cart.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.cart.application.dto.req.CreateCartRequest;
import com.programmers.smrtstore.domain.cart.application.dto.req.UpdateCartRequest;
import com.programmers.smrtstore.domain.cart.application.dto.res.CartResponse;
import com.programmers.smrtstore.domain.cart.application.dto.res.CreateCartResponse;
import com.programmers.smrtstore.domain.cart.domain.entity.Cart;
import com.programmers.smrtstore.domain.cart.exception.CartException;
import com.programmers.smrtstore.domain.cart.infrastructure.CartJPARepository;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJPARepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartJPARepository cartJPARepository;
    private final ProductJPARepository productJPARepository;
    private final UserRepository userRepository;

    public CreateCartResponse createCart(CreateCartRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new UserException(
                ErrorCode.USER_NOT_FOUND, null));
        Product product = productJPARepository.findById(request.getProductId())
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        cartJPARepository.findByUserAndProduct(user, product).ifPresent(cart -> {
            throw new CartException(ErrorCode.CART_ALREADY_EXIST);
        });
        Cart cart = cartJPARepository.save(
            Cart.builder()
                .user(user)
                .product(product)
                .quantity(request.getQuantity())
                .build()
        );
        return CreateCartResponse.from(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCartById(Long cartId) {
        Cart cart = getCart(cartId);
        return CartResponse.from(cart);
    }

    @Transactional(readOnly = true)
    public List<CartResponse> getAllCarts(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(
                ErrorCode.USER_NOT_FOUND, null));

        return cartJPARepository.findByUser(user)
            .stream()
            .map(CartResponse::from)
            .toList();
    }

    public CartResponse updateCartQuantity(UpdateCartRequest request) {
        Cart cart = getCart(request.getCartId());
        if (request.isAddYn()) {
            cart.addQuantity(request.getQuantity());
        } else {
            cart.removeQuantity(request.getQuantity());
        }
        return CartResponse.from(cart);
    }

    public void deleteCart(Long cartId) {
        Cart cart = getCart(cartId);
        cartJPARepository.delete(cart);
    }

    private Cart getCart(Long cartId) {
        return cartJPARepository.findById(cartId)
            .orElseThrow(() -> new CartException(
                ErrorCode.CART_NOT_FOUND));
    }

}
