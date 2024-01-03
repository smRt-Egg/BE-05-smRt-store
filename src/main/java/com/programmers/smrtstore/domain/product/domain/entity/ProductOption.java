package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionTag;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_tag", nullable = false)
    private OptionTag optionTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_type", nullable = false)
    private OptionType optionType;

    @Column(name = "option_name", nullable = false, length = 50)
    private String optionName;

    @Column(name = "price", nullable = false)
    private Integer price;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "product_quantity")
    private ProductQuantity productQuantity;

    @CreationTimestamp
    @Column(name = "register_date", nullable = false)
    private LocalDateTime registerDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public ProductOption(String optionName, Integer price, OptionTag optionTag,
        OptionType optionType, Integer stockQuantity, Product product) {
        this.optionName = optionName;
        this.price = price == null ? 0 : price;
        this.optionTag = optionTag;
        this.optionType = optionType;
        this.productQuantity = ProductQuantity.from(stockQuantity == null ? 0 : stockQuantity);
        this.product = product;
        if (optionType.equals(OptionType.SUPPLEMENTED)) {
            this.product.addOption(this);
        } else {
            this.product.addAdditionalOption(this);
        }

    }

    public void addStockQuantity(Integer quantity) {
        this.productQuantity.addStockQuantity(quantity);
    }

    public void removeStockQuantity(Integer quantity) {
        this.productQuantity.removeStockQuantity(quantity);
    }

    protected void updateStockQuantity(Integer quantity) {
        this.productQuantity.updateStockQuantity(quantity);
    }

    public Integer getStockQuantity() {
        return this.productQuantity.getStockQuantity();
    }

    private void updateOptionName(String optionName) {
        this.optionName = optionName;
    }

    private void updatePrice(Integer price) {
        this.price = price;
    }

    private void updateOptionTag(OptionTag optionTag) {
        this.optionTag = optionTag;
    }

    public void updateValues(String optionName, Integer price, OptionTag optionTag) {
        if (optionName != null) {
            updateOptionName(optionName);
        }
        if (price != null) {
            updatePrice(price);
        }
        if (optionTag != null) {
            updateOptionTag(optionTag);
        }
    }
}
