package com.programmers.smrtstore.domain.product.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@Table(name = "option_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "optionName", nullable = false, length = 50)
    private String optionName;

    @Column(name = "price", nullable = false)
    private Integer price = 0;

    @Column(name = "stockQuantity", nullable = false)
    private Integer stockQuantity;

    @CreationTimestamp
    @Column(name = "register_date", nullable = false, updatable = false)
    private Timestamp registerDate;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Product product;

    @Builder
    private Option(String optionName, Integer price, Integer stockQuantity, Product product) {
        this.optionName = optionName;
        this.price = price == null ? 0 : price;
        this.stockQuantity = stockQuantity;
        this.product = product;
    }

    void addStockQuantity(Integer quantity) {
        this.stockQuantity += quantity;
    }

    void removeStockQuantity(Integer quantity) {
        if (this.stockQuantity < quantity) {
            // TODO: using Custom Exception
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        this.stockQuantity -= quantity;
    }
}
