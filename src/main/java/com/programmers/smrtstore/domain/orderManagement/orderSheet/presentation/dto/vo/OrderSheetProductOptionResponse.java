package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.vo;

import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNames;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderSheetProductOptionResponse {

    private Long productOptionId;
    private OptionNames optionNames;
    private Integer extraPrice;
    private Integer quantity;
    private OptionType optionType;

    @Builder
    public OrderSheetProductOptionResponse(
        Long productOptionId, OptionNames optionNames, Integer extraPrice, Integer quantity,
        OptionType optionType
    ) {
        this.productOptionId = productOptionId;
        this.optionNames = optionNames;
        this.extraPrice = extraPrice;
        this.quantity = quantity;
        this.optionType = optionType;
    }

    public static OrderSheetProductOptionResponse of(
        Long productOptionId, OptionNames optionNames, Integer extraPrice, Integer quantity,
        OptionType optionType
    ) {
        return OrderSheetProductOptionResponse.builder()
            .productOptionId(productOptionId)
            .optionNames(optionNames)
            .extraPrice(extraPrice)
            .quantity(quantity)
            .optionType(optionType)
            .build();
    }
}
