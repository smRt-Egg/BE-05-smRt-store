package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAdditionalOption extends ProductOption {

    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "group_name", length = 50)
    private String groupName;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "name", length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status_type")
    private ProductStatusType productStatusType;

    @Builder
    private ProductAdditionalOption(@Valid @NotBlank String groupName, @Valid @NotBlank String name,
        Product product, Integer stockQuantity, Integer price) {
        super(stockQuantity, price, product);
        this.groupName = groupName;
        this.name = name;
        this.productStatusType = ProductStatusType.SALE;
    }

    public void updateValues(Integer quantity, Integer price, String groupName, String name) {
        if (quantity != null) {
            productStatusType =
                quantity == 0 ? ProductStatusType.OUT_OF_STOCK : ProductStatusType.SALE;
        }
        super.updateValues(quantity, price);
        if (groupName != null) {
            setGroupName(groupName);
        }
        if (name != null) {
            setName(name);
        }
    }

    @Override
    public Integer removeStockQuantity(Integer quantity) {
        int result = super.removeStockQuantity(quantity);
        if (result == 0) {
            productStatusType = ProductStatusType.OUT_OF_STOCK;
        }
        return result;
    }

    @Override
    public Integer addStockQuantity(Integer quantity) {
        int result = super.addStockQuantity(quantity);
        if (result != 0) {
            productStatusType = ProductStatusType.SALE;
        }
        return result;
    }
}