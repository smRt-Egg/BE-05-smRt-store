package com.programmers.smrtstore.domain.cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.programmers.smrtstore.domain.cart.application.dto.req.CreateCartRequest;
import com.programmers.smrtstore.domain.cart.application.dto.req.UpdateCartRequest;
import com.programmers.smrtstore.domain.cart.domain.entity.Cart;
import com.programmers.smrtstore.domain.cart.exception.CartException;
import com.programmers.smrtstore.domain.cart.infrastructure.CartJPARepository;
import com.programmers.smrtstore.domain.product.domain.entity.Category;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.product.infrastructure.ProductJPARepository;
import com.programmers.smrtstore.domain.user.domain.entity.Gender;
import com.programmers.smrtstore.domain.user.domain.entity.Role;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DisplayName("Test Cart Service")
@Testcontainers
@Transactional
class CartServiceTest {

    private static final Integer PRODUCT_PRICE = 10000;
    private static final Integer CART_QUANTITY = 3;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartJPARepository cartJPARepository;
    @Autowired
    private ProductJPARepository productJPARepository;
    @Autowired
    private UserRepository userRepository;
    private User expectedUser;
    private Product expectedProduct;

    @BeforeEach
    void init() throws MalformedURLException {
        expectedUser = userRepository.save(User.builder()
            .nickName("test")
            .email("test@gmail.com")
            .phone("01012345678")
            .birth("20231231")
            .gender(Gender.MALE)
            .role(Role.ROLE_USER)
            .point(0)
            .marketingAgree(true)
            .membershipYN(true)
            .repurchaseYN(true)
            .build());
        expectedProduct = productJPARepository.save(Product.builder()
            .name("test")
            .price(PRODUCT_PRICE)
            .stockQuantity(100)
            .optionYn(false)
            .category(Category.TEMP)
            .thumbnail(new URL("http://localhost:8080"))
            .contentImage(new URL("http://localhost"))
            .build());
    }

    @Test
    void testCreateCartSuccess() {
        // Arrange
        CreateCartRequest expectedRequest = CreateCartRequest.builder()
            .userId(expectedUser.getId())
            .productId(expectedProduct.getId())
            .quantity(CART_QUANTITY)
            .build();
        // Act
        var actualResult = cartService.createCart(expectedRequest);
        // Assert
        assertThat(actualResult.getId()).isNotNull();
        assertThat(actualResult.getUserId()).isEqualTo(expectedUser.getId());
        assertThat(actualResult.getProductId()).isEqualTo(expectedProduct.getId());
        assertThat(actualResult.getQuantity()).isEqualTo(expectedRequest.getQuantity());
        assertThat(actualResult.getPrice()).isEqualTo(
            PRODUCT_PRICE * expectedRequest.getQuantity());
        assertThat(actualResult.getCreatedAt()).isNotNull();
    }

    @Test
    void testCreateCartFail() {
        // Arrange
        cartJPARepository.save(Cart.builder()
            .user(expectedUser)
            .product(expectedProduct)
            .quantity(CART_QUANTITY)
            .build());
        CreateCartRequest expectedRequest = CreateCartRequest.builder()
            .userId(expectedUser.getId())
            .productId(expectedProduct.getId())
            .quantity(CART_QUANTITY)
            .build();
        // Act & Assert
        assertThrows(CartException.class, () -> cartService.createCart(expectedRequest));
    }

    @Test
    void testGetCartById() {
        // Arrange
        Cart expectedCart = cartJPARepository.save(Cart.builder()
            .user(expectedUser)
            .product(expectedProduct)
            .quantity(CART_QUANTITY)
            .build());
        // Act
        var actualResult = cartService.getCartById(expectedCart.getId());
        // Assert
        assertThat(actualResult.getId()).isEqualTo(expectedCart.getId());
        assertThat(actualResult.getUserId()).isEqualTo(expectedUser.getId());
        assertThat(actualResult.getProductId()).isEqualTo(expectedProduct.getId());
        assertThat(actualResult.getQuantity()).isEqualTo(expectedCart.getQuantity());
        assertThat(actualResult.getPrice()).isEqualTo(PRODUCT_PRICE * expectedCart.getQuantity());
        assertThat(actualResult.getCreatedAt()).isNotNull();
    }

    @Test
    void testGetAllCarts() {
        // Arrange
        Cart expectedCart = cartJPARepository.save(Cart.builder()
            .user(expectedUser)
            .product(expectedProduct)
            .quantity(CART_QUANTITY)
            .build());
        // Act
        var actualResult = cartService.getAllCarts(expectedUser.getId());
        // Assert
        assertThat(actualResult).isNotEmpty().hasSize(1);
        assertThat(actualResult.get(0).getId()).isEqualTo(expectedCart.getId());
        assertThat(actualResult.get(0).getUserId()).isEqualTo(expectedUser.getId());
        assertThat(actualResult.get(0).getProductId()).isEqualTo(expectedProduct.getId());
        assertThat(actualResult.get(0).getQuantity()).isEqualTo(expectedCart.getQuantity());
        assertThat(actualResult.get(0).getPrice()).isEqualTo(
            PRODUCT_PRICE * expectedCart.getQuantity());
        assertThat(actualResult.get(0).getCreatedAt()).isNotNull();
    }

    @DisplayName("test updateCartQuantity with add quantity success")
    @Test
    void testUpdateCartQuantitySuccessCase1() {
        // Arrange
        int expectedQuantity = 1;
        Cart expectedCart = cartJPARepository.save(Cart.builder()
            .user(expectedUser)
            .product(expectedProduct)
            .quantity(CART_QUANTITY)
            .build());
        UpdateCartRequest expectedRequest = UpdateCartRequest.builder()
            .cartId(expectedCart.getId())
            .addYn(true)
            .quantity(expectedQuantity)
            .build();
        // Act
        var actualResult = cartService.updateCartQuantity(expectedRequest);
        // Assert
        assertThat(actualResult.getQuantity()).isEqualTo(CART_QUANTITY + expectedQuantity);
        assertThat(actualResult.getUpdatedAt()).isNotNull();
    }

    @DisplayName("test updateCartQuantity with subtract quantity success")
    @Test
    void testUpdateCartQuantitySuccessCase2() {
        // Arrange
        int expectedQuantity = 1;
        Cart expectedCart = cartJPARepository.save(Cart.builder()
            .user(expectedUser)
            .product(expectedProduct)
            .quantity(CART_QUANTITY)
            .build());
        UpdateCartRequest expectedRequest = UpdateCartRequest.builder()
            .cartId(expectedCart.getId())
            .addYn(false)
            .quantity(expectedQuantity)
            .build();
        // Act
        var actualResult = cartService.updateCartQuantity(expectedRequest);
        // Assert
        assertThat(actualResult.getQuantity()).isEqualTo(CART_QUANTITY - expectedQuantity);
        assertThat(actualResult.getUpdatedAt()).isNotNull();
    }

    @Test
    void testUpdateCartQuantityFail() {
        // Arrange
        int expectedQuantity = 5;
        Cart expectedCart = cartJPARepository.save(Cart.builder()
            .user(expectedUser)
            .product(expectedProduct)
            .quantity(CART_QUANTITY)
            .build());
        UpdateCartRequest expectedRequest = UpdateCartRequest.builder()
            .cartId(expectedCart.getId())
            .addYn(false)
            .quantity(expectedQuantity)
            .build();
        // Act & Assert
        assertThrows(CartException.class, () -> cartService.updateCartQuantity(expectedRequest));
    }

    @Test
    void testDeleteCart() {
        // Arrange
        Cart expectedCart = cartJPARepository.save(Cart.builder()
            .user(expectedUser)
            .product(expectedProduct)
            .quantity(CART_QUANTITY)
            .build());
        // Act
        cartService.deleteCart(expectedCart.getId());
        // Assert
        assertThat(cartJPARepository.findById(expectedCart.getId())).isEmpty();
    }
}