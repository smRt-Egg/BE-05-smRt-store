package com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderSheetProductOptionReq {

    @NotNull
    private Long optionId;
    @Positive
    @NotNull
    private Integer quantity;
    @PositiveOrZero
    @NotNull
    private Integer extraPrice;

}
