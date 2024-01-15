package com.programmers.smrtstore.domain.product.presentation.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class ProductDiscountRatioAPIRequest {

    @NotNull
    @PositiveOrZero(message = "할인률은 0 이상이어야 합니다.")
    @Max(value = 100, message = "할인률은 100 이하여야 합니다.")
    Integer discountRatio;
}
