package com.programmers.smrtstore.domain.product.application.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.enums.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductRequest {

    private Long id;
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private Category category;
    private String thumbnail;
    private String contentImage;
    private String optionNameType1;
    private String optionNameType2;
    private String optionNameType3;
}
