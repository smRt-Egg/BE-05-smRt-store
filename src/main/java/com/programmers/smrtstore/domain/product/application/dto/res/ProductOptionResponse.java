package com.programmers.smrtstore.domain.product.application.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.OptionTag;
import com.programmers.smrtstore.domain.product.domain.entity.ProductOption;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductOptionResponse {

    private Long id;
    private OptionTag optionTag;
    private String optionName;
    private Integer price;
    private Integer stockQuantity;
    private Timestamp registerDate;

    public static ProductOptionResponse from(ProductOption productOption) {
        return new ProductOptionResponse(
            productOption.getId(),
            productOption.getOptionTag(),
            productOption.getOptionName(),
            productOption.getPrice(),
            productOption.getStockQuantity(),
            productOption.getRegisterDate()
        );
    }
}
