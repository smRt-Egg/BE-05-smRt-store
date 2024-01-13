package com.programmers.smrtstore.domain.orderManagement.orderSheet.infrastructure;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.domain.entity.OrderSheet;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSheetJpaRepository extends JpaRepository<OrderSheet, Long> {

    @EntityGraph(attributePaths = {"orderedProducts", "orderedProducts.productOption",
        "orderedProducts.product", "orderedProducts.productOption.productQuantity"})
    Optional<OrderSheet> getOrderSheetWithOrderedProductsById(Long orderSheetId);

}
