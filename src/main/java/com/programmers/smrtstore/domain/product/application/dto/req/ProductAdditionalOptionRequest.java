package com.programmers.smrtstore.domain.product.application.dto.req;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductAdditionalOptionRequest {
    private Long id;
    private Integer quantity;
    private Integer price;
    private String groupName;
    private String name;
}
