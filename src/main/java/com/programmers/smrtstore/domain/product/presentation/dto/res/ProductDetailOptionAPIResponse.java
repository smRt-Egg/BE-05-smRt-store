package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailOptionResponse;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailOptionAPIResponse {

    private Long id;
    private String optionName1;
    private String optionName2;
    private String optionName3;
    private String optionType;
    private Integer price;
    private Integer stockQuantity;
    private LocalDateTime registerDate;

    public static ProductDetailOptionAPIResponse from(ProductDetailOptionResponse response) {
        return new ProductDetailOptionAPIResponse(
            response.getId(),
            response.getOptionNames().getOptionName1(),
            response.getOptionNames().getOptionName2(),
            response.getOptionNames().getOptionName3(),
            response.getOptionType().name(),
            response.getPrice(),
            response.getStockQuantity(),
            response.getRegisterDate()
        );
    }
}
