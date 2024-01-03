package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNames;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetailOption extends ProductOption {

    @Embedded
    @Setter(value = AccessLevel.PRIVATE)
    private OptionNames optionNames;

    @CreationTimestamp
    @Column(name = "register_date", nullable = false, updatable = false)
    private LocalDateTime registerDate;

    @Builder
    private ProductDetailOption(Integer stockQuantity, Integer price, Product product,
        @NotNull OptionNames optionNames) {
        super(stockQuantity, price, product);
        this.optionNames = optionNames;
        this.getProduct().addOption(this);
    }

    public void updateValues(Integer quantity, Integer price, OptionNames optionNames) {
        super.updateValues(quantity, price);
        if (optionNames != null) {
            setOptionNames(optionNames);
        }
    }

}
