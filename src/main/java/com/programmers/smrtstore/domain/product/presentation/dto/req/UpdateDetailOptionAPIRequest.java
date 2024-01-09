package com.programmers.smrtstore.domain.product.presentation.dto.req;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UpdateDetailOptionAPIRequest {

    private String optionName1;
    private String optionName2;
    private String optionName3;
    @Min(value = 0, message = "수량은 0 이상이 되어야 합니다.")
    private Integer quantity;
    @Min(value = 0, message = "가격은 0 이상이 되어야 합니다.")
    private Integer price;
}
