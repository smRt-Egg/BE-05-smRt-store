package com.programmers.smrtstore.domain.product.domain.entity;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNames;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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
        OptionType optionType, OptionNames optionNames) {
        super(stockQuantity, price, optionType, product);
        if (optionType.equals(OptionType.ADDITIONAL)) {
            throw new ProductException(ErrorCode.PRODUCT_OPTION_TYPE_INVALID);
        }
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
