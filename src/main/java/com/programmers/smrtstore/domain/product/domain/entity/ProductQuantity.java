package com.programmers.smrtstore.domain.product.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    private ProductQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void addStockQuantity(Integer quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStockQuantity(Integer quantity) {
        if (this.stockQuantity < quantity) {
            // TODO: using custom exception
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
    }
}