package com.programmers.smrtstore.domain.cart.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.cart.application.dto.req.CreateCartRequest;
import com.programmers.smrtstore.domain.cart.application.dto.req.UpdateCartOptionRequest;
import com.programmers.smrtstore.domain.cart.application.dto.req.UpdateCartQuantityRequest;
import com.programmers.smrtstore.domain.cart.application.dto.res.CartResponse;
import com.programmers.smrtstore.domain.cart.application.dto.res.CreateCartResponse;
import com.programmers.smrtstore.domain.cart.domain.entity.Cart;
import com.programmers.smrtstore.domain.cart.exception.CartException;
import com.programmers.smrtstore.domain.cart.infrastructure.CartJPARepository;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductDetailOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
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
    private final ProductJpaRepository productJPARepository;
    private final ProductDetailOptionJpaRepository detailOptionJpaRepository;
    private final UserRepository userRepository;

    public CreateCartResponse createCart(CreateCartRequest request) {
        var user = getUser(request.getUserId());
        Product product = productJPARepository.findById(request.getProductId())
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        var detailOption = detailOptionJpaRepository.findById(request.getDetailOptionId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        Cart cart = cartJPARepository.findByUserAndProduct(user, product)
            .orElseGet(() -> cartJPARepository.save(
                Cart.of(user, product, detailOption)));
        cart.updateQuantity(request.getQuantity(), request.getUserId());
        return CreateCartResponse.from(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCartById(Long cartId) {
        Cart cart = getCart(cartId);
        return CartResponse.from(cart);
    }

    @Transactional(readOnly = true)
    public List<CartResponse> getAllCarts(Long userId) {
        var user = getUser(userId);
        return cartJPARepository.findByUser(user)
            .stream()
            .map(CartResponse::from)
            .toList();
    }

    public CartResponse updateCartQuantity(UpdateCartQuantityRequest request) {
        Cart cart = getCart(request.getCartId());
        cart.updateQuantity(request.getQuantity(), request.getUserId());
        return CartResponse.from(cart);
    }


    public CartResponse updateCartProductOption(UpdateCartOptionRequest request) {
        Cart cart = getCart(request.getCartId());
        var detailOption = detailOptionJpaRepository.findById(request.getProductDetailOptionId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        cart.updateDetailOption(detailOption, request.getUserId());
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

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(
                ErrorCode.USER_NOT_FOUND));
    }

}
