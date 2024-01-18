package com.programmers.smrtstore.domain.orderManagement.orderedProduct.application.dto;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.OrderSheetProductOptionReq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateOrderedProductRequest {

    @NotNull
    private Long productId;
    // 원가격
    @PositiveOrZero
    @NotNull
    private Integer orgPrice;
    // 즉시 할인 비율
    @PositiveOrZero
    @NotNull
    private Integer sellerImmediateDiscount;
    @Valid
    @NotNull
    private OrderSheetProductOptionReq option;
    @Positive
    @NotNull
    private Integer quantity;
}
