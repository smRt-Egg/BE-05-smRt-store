package com.programmers.smrtstore.domain.product.application.dto.req;

import com.programmers.smrtstore.domain.product.domain.entity.vo.OptionNames;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductDetailOptionRequest {

    @Getter
    private Long id;
    private String optionName1;
    private String optionName2;
    private String optionName3;
    @Getter
    private Integer price;
    @Getter
    private Integer quantity;

    public OptionNames getOptionNames() {
        return OptionNames.builder()
            .optionName1(optionName1)
            .optionName2(optionName2)
            .optionName3(optionName3)
            .build();
    }
}
