package com.programmers.smrtstore.domain.product.presentation.dto.req;

import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductDetailOptionRequest;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CreateProductDetailOptionAPIRequest {

    private String optionName1;
    private String optionName2;
    private String optionName3;

    @Min(value = 0, message = "상품 옵션 가격은 0원 이상이어야 합니다.")
    private Integer price;

    @Min(value = 0, message = "상품 옵션 수량은 0개 이상이어야 합니다.")
    private Integer stockQuantity;

    public CreateProductDetailOptionRequest toRequest() {
        return CreateProductDetailOptionRequest.builder()
            .optionName1(optionName1)
            .optionName2(optionName2)
            .optionName3(optionName3)
            .price(price)
            .stockQuantity(stockQuantity)
            .build();
    }

}
