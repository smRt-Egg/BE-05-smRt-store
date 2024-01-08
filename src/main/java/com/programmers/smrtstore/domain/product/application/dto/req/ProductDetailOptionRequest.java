package com.programmers.smrtstore.domain.product.application.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailOptionRequest {

    private Long id;
    private String optionName1;
    private String optionName2;
    private String optionName3;
    private Integer price;
    private Integer quantity;

}
