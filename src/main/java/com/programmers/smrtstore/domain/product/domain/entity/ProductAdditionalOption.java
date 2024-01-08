package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.domain.entity.enums.ProductStatusType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductAdditionalOption extends ProductOption {

    @Column(name = "group_name", length = 50)
    private String groupName;

    @Column(name = "name", length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_status_type")
    private ProductStatusType productStatusType;

    @Builder
    private ProductAdditionalOption(String groupName, String name, Product product,
        Integer stockQuantity, Integer price) {
        super(stockQuantity, price, OptionType.ADDITIONAL, product);
        this.groupName = groupName;
        this.name = name;
        this.productStatusType = ProductStatusType.SALE;
        this.getProduct().addAdditionalOption(this);
    }

    @Override
    public void updateStockQuantity(Integer quantity) {
        super.updateStockQuantity(quantity);
        if (quantity != null && quantity >= 0) {
            productStatusType =
                quantity == 0 ? ProductStatusType.OUT_OF_STOCK : ProductStatusType.SALE;
        }
    }

    public void updateGroupName(String groupName) {
        if (groupName != null && !groupName.isBlank()) {
            this.groupName = groupName;
        }
    }

    public void updateName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
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