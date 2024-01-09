package com.programmers.smrtstore.domain.product.domain.entity.vo;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionNames {

    @Column(name = "option_name_1", length = 50)
    private String optionName1;

    @Column(name = "option_name_2", length = 50)
    private String optionName2;

    @Column(name = "option_name_3", length = 50)
    private String optionName3;

    @Builder
    private OptionNames(int optionSize, String optionName1, String optionName2,
        String optionName3) {
        verifyOptionNames(optionSize, optionName1, optionName2, optionName3);
        this.optionName1 = optionName1;
        this.optionName2 = optionName2;
        this.optionName3 = optionName3;
    }

    private static int getOptionSize(String optionName1, String optionName2, String optionName3) {
        return Stream.of(optionName1, optionName2, optionName3)
            .filter(Objects::nonNull)
            .filter(option -> !option.isBlank())
            .mapToInt(option -> 1)
            .sum();
    }

    private static void verifyOptionNames(int optionSize, String optionName1, String optionName2,
        String optionName3) {
        if (optionSize == 1) {
            if (optionName1 == null || optionName1.isBlank()) {
                throw new ProductException(ErrorCode.PRODUCT_OPTION_NAME_INVALID);
            }
        } else if (optionSize == 2) {
            if (optionName2 == null || optionName2.isBlank() || optionName2.equals(
                optionName1)) {
                throw new ProductException(ErrorCode.PRODUCT_OPTION_NAME_INVALID);
            }
        } else if (optionSize == 3 && (optionName3 == null || optionName3.isBlank()
            || optionName3.equals(
            optionName1) || optionName3.equals(optionName2))) {
            throw new ProductException(ErrorCode.PRODUCT_OPTION_NAME_INVALID);
        }

    }

    public void updateOptionNames(String optionName1, String optionName2, String optionName3) {
        verifyOptionNames(getOptionSize(optionName1, optionName2, optionName3), optionName1,
            optionName2, optionName3);
        this.optionName1 = optionName1;
        this.optionName2 = optionName2;
        this.optionName3 = optionName3;
    }

}