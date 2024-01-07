package com.programmers.smrtstore.domain.product.application.dto.res;

import com.programmers.smrtstore.domain.product.domain.entity.ProductDetailOption;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNames;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailOptionResponse {

    private Long id;
    private OptionNames optionNames;
    private OptionType optionType;
    private Integer price;
    private Integer stockQuantity;
    private LocalDateTime registerDate;

    public static ProductDetailOptionResponse from(ProductDetailOption detailOption) {
        return new ProductDetailOptionResponse(
            detailOption.getId(),
            detailOption.getOptionNames(),
            detailOption.getOptionType(),
            detailOption.getPrice(),
            detailOption.getStockQuantity(),
            detailOption.getRegisterDate()
        );
    }
}
