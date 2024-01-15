package com.programmers.smrtstore.domain.product.presentation.dto.res;

import com.programmers.smrtstore.domain.product.application.dto.res.ProductDetailOptionResponse;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNames;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailOptionAPIResponse {

    private Long id;
    private String optionName1;
    private String optionName2;
    private String optionName3;
    private String optionType;
    private Integer price;
    private Integer stockQuantity;
    private LocalDateTime registerDate;

    @Builder(access = AccessLevel.PRIVATE)
    private ProductDetailOptionAPIResponse(Long id, OptionNames optionNames, OptionType optionType,
        Integer price, Integer stockQuantity,
        LocalDateTime registerDate) {
        this.id = id;
        if (optionNames != null) {
            this.optionName1 = optionNames.getOptionName1();
            this.optionName2 = optionNames.getOptionName2();
            this.optionName3 = optionNames.getOptionName3();
        }

        this.optionType = optionType.name();
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.registerDate = registerDate;
    }

    public static ProductDetailOptionAPIResponse from(ProductDetailOptionResponse response) {
        return ProductDetailOptionAPIResponse.builder()
            .id(response.getId())
            .optionNames(response.getOptionNames())
            .optionType(response.getOptionType())
            .price(response.getPrice())
            .stockQuantity(response.getStockQuantity())
            .registerDate(response.getRegisterDate())
            .build();
    }
}
