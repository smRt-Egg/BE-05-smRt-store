package com.programmers.smrtstore.domain.orderManagement.orderedProduct.application.dto;

import com.programmers.smrtstore.domain.orderManagement.orderSheet.presentation.dto.req.OrderSheetProductOptionReq;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateOrderedProductRequest {

    @NotNull
    private Long productId;
    // 즉시할인 적용 된 값
    @NotNull
    private Integer salePrice;
    // 원가격
    @NotNull
    private Integer orgPrice;
    // 즉시 할인 비율
    @NotNull
    private Integer sellerImmediateDiscountRatio;
    @Valid
    @NotNull
    private OrderSheetProductOptionReq option;
    @NotNull
    private Integer quantity;
    @NotNull
    private Integer totalPrice;
}
