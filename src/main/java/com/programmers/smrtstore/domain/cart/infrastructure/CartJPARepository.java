package com.programmers.smrtstore.domain.cart.infrastructure;

import com.programmers.smrtstore.domain.cart.domain.entity.Cart;
import com.programmers.smrtstore.domain.product.domain.entity.Product;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartJPARepository extends JpaRepository<Cart, Long> {

    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProduct(User user, Product product);
}