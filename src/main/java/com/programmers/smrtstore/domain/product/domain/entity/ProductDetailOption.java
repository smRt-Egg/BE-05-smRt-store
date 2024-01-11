package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNames;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@Table(name = "product_option_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetailOption {

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

    @Embedded
    private OptionNames optionNames;

    @CreationTimestamp
    @Column(name = "register_date", nullable = false, updatable = false)
    private LocalDateTime registerDate;

    @Builder
    private ProductDetailOption(Integer stockQuantity, Integer price, Product product,
        OptionType optionType, OptionNames optionNames) {
        this.productQuantity = ProductQuantity.from(stockQuantity);
        this.price = price;
        this.optionType = optionType;
        this.product = product;
        this.optionNames = optionNames;
        this.getProduct().addOption(this);
    }

    public void updateOptionNames(String optionName1, String optionName2, String optionName3) {
        optionNames.updateOptionNames(optionName1, optionName2, optionName3);
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
