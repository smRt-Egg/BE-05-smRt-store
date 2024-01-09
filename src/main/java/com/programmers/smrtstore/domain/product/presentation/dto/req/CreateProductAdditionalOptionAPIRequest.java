package com.programmers.smrtstore.domain.product.presentation.dto.req;

import com.programmers.smrtstore.domain.product.application.dto.req.CreateProductAdditionalOptionRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateProductAdditionalOptionAPIRequest {

    @NotBlank
    private String groupName;
    @NotBlank
    private String name;
    @Min(value = 0, message = "상품 옵션 가격은 0원 이상이어야 합니다.")
    private Integer price;
    @Min(value = 0, message = "상품 옵션 수량은 0개 이상이어야 합니다.")
    private Integer stockQuantity;

    public CreateProductAdditionalOptionRequest toRequest() {
        return CreateProductAdditionalOptionRequest.builder()
            .groupName(groupName)
            .name(name)
            .price(price)
            .stockQuantity(stockQuantity)
            .build();
    }
}
