package com.programmers.smrtstore.domain.product.domain.entity;

import static com.programmers.smrtstore.core.properties.ErrorCode.PRODUCT_QUANTITY_NOT_ENOUGH;

import com.programmers.smrtstore.domain.product.exception.ProductException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product_quantity_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductQuantity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    private ProductQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public static ProductQuantity from(Integer stockQuantity) {
        return new ProductQuantity(stockQuantity);
    }

    public void addStockQuantity(Integer quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStockQuantity(Integer quantity) {
        if (this.stockQuantity < quantity) {
            throw new ProductException(PRODUCT_QUANTITY_NOT_ENOUGH);
        }
        this.stockQuantity -= quantity;
    }
}