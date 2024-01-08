package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_option_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ProductOption {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_quantity_id")
    private ProductQuantity productQuantity;

    @Getter
    @Column(name = "price", nullable = false)
    private Integer price;

    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "option_type", nullable = false)
    private OptionType optionType;


    protected ProductOption(Integer stockQuantity, Integer price, OptionType optionType,
        Product product) {
        this.productQuantity = ProductQuantity.from(stockQuantity);
        this.price = price;
        this.optionType = optionType;
        this.product = product;
    }

    public Integer getStockQuantity() {
        return productQuantity.getStockQuantity();
    }

    public Integer addStockQuantity(Integer quantity) {
        productQuantity.addStockQuantity(quantity);
        return getStockQuantity();
    }

    public Integer removeStockQuantity(Integer quantity) {
        productQuantity.removeStockQuantity(quantity);
        return getStockQuantity();
    }

    public void updateStockQuantity(Integer quantity) {
        productQuantity.setStockQuantity(quantity);
    }

    public void updatePrice(Integer price) {
        if (price >= 0) {
            this.price = price;
        }
    }
}