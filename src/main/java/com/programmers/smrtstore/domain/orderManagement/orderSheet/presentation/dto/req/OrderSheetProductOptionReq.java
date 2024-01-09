package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderSheetProductOptionReq {

    @NotNull
    private Long optionId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Integer extraPrice;
    @NotNull
    private OptionType optionType;

}
