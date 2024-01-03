package com.programmers.smrtstore.domain.product.application.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.ProductOption;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductOptionResponse {

    private Long id;
    private OptionType optionType;
    private String optionName;
    private Integer price;
    private Integer stockQuantity;
    private LocalDateTime registerDate;

    public static ProductOptionResponse from(ProductOption productOption) {
        return new ProductOptionResponse(
            productOption.getId(),
            productOption.getOptionType(),
            productOption.getOptionName(),
            productOption.getPrice(),
            productOption.getStockQuantity(),
            productOption.getRegisterDate()
        );
    }
}
