package com.programmers.smrtstore.domain.cart.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.cart.domain.entity.Cart;
import com.programmers.smrtstore.domain.cart.exception.CartException;
import com.programmers.smrtstore.domain.cart.infrastructure.CartJPARepository;
import com.programmers.smrtstore.domain.cart.presentation.dto.req.CreateCartRequest;
import com.programmers.smrtstore.domain.cart.presentation.dto.req.UpdateCartOptionRequest;
import com.programmers.smrtstore.domain.cart.presentation.dto.req.UpdateCartQuantityRequest;
import com.programmers.smrtstore.domain.cart.presentation.dto.res.CartResponse;
import com.programmers.smrtstore.domain.cart.presentation.dto.res.CreateCartResponse;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import com.programmers.smrtstore.domain.product.infrastructure.ProductDetailOptionJpaRepository;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJpaRepository;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
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
    private final UserJpaRepository userJpaRepository;

    public CreateCartResponse createCart(Long userId, CreateCartRequest request) {
        var user = getUser(userId);
        Product product = productJPARepository.findByIdAndProductStatusType(request.getProductId(),
                ProductStatusType.SALE)
            .orElseThrow(() -> new ProductException(
                ErrorCode.PRODUCT_NOT_FOUND));
        var detailOption = detailOptionJpaRepository.findById(request.getDetailOptionId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        Cart cart = cartJPARepository.findByUserAndProduct(user, product)
            .orElseGet(() -> cartJPARepository.save(
                Cart.of(user, product, detailOption)));
        cart.updateQuantity(request.getQuantity(), userId);
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
            .filter(cart -> cart.getProduct().getProductStatusType().equals(ProductStatusType.SALE))
            .map(CartResponse::from)
            .toList();
    }

    public CartResponse updateCartQuantity(Long cartId, Long userId,
        UpdateCartQuantityRequest request) {
        Cart cart = getCart(cartId);
        validateProduct(cart.getProduct());
        cart.updateQuantity(request.getQuantity(), userId);
        return CartResponse.from(cart);
    }


    public CartResponse updateCartProductOption(Long userId, Long cartId,
        UpdateCartOptionRequest request) {
        Cart cart = getCart(cartId);
        validateProduct(cart.getProduct());
        var detailOption = detailOptionJpaRepository.findById(request.getProductDetailOptionId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
        cart.updateDetailOption(detailOption, userId);
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
        return userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserException(
                ErrorCode.USER_NOT_FOUND));
    }

    private void validateProduct(Product product) {
        if (!product.getProductStatusType().equals(ProductStatusType.SALE)) {
            throw new CartException(ErrorCode.PRODUCT_NOT_AVAILABLE);
        }
    }

}
