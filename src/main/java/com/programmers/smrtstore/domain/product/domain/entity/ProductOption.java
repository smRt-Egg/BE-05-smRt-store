package com.programmers.smrtstore.domain.product.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "category_option_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_tag", nullable = false)
    private OptionTag optionTag;

    @Column(name = "option_name", nullable = false, length = 50)
    private String optionName;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @CreationTimestamp
    @Column(name = "register_date", nullable = false)
    private Timestamp registerDate;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public ProductOption(OptionTag optionTag, String optionName, Integer price,
        Integer stockQuantity, Product product) {
        this.optionTag = optionTag;
        this.optionName = optionName;
        this.price = price == null ? 0 : price;
        this.stockQuantity = stockQuantity;
        this.product = product;
    }

    public void addStockQuantity(Integer quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStockQuantity(Integer quantity) {
        if (this.stockQuantity < quantity) {
            // TODO: using Custom Exception
            throw new IllegalArgumentException();
        }
        this.stockQuantity -= quantity;
    }
}
