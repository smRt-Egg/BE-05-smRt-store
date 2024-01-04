package com.programmers.smrtstore.domain.product.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Setter;

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
    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "price", nullable = false)
    private Integer price;

    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected ProductOption(Integer stockQuantity, Integer price, Product product) {
        this.productQuantity = ProductQuantity.from(stockQuantity);
        this.price = price;
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

    protected void setStockQuantity(Integer quantity) {
        productQuantity.setStockQuantity(quantity);
    }

    protected void updateValues(Integer quantity, Integer price) {
        if (quantity != null) {
            setStockQuantity(quantity);
        }
        if (price != null) {
            setPrice(price);
        }
    }
}