package com.programmers.smrtstore.domain.product.domain.entity.vo;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.product.domain.entity.enums.OptionType;
import com.programmers.smrtstore.domain.product.exception.ProductException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionNameTypes {

    @Enumerated(EnumType.STRING)
    @Column(name = "option_type", nullable = false)
    private OptionType optionType;

    @Column(name = "option_name_type_1", length = 50)
    private String optionNameType1;

    @Column(name = "option_name_type_2", length = 50)
    private String optionNameType2;

    @Column(name = "option_name_type_3", length = 50)
    private String optionNameType3;

    @Column(name = "size", nullable = false)
    @JdbcTypeCode(SqlTypes.TINYINT)
    private Integer size;

    @Builder
    private OptionNameTypes(OptionType optionType, String optionNameType1, String optionNameType2,
        String optionNameType3) {
        this.optionType = optionType;
        if (optionType.equals(OptionType.SINGLE) && (optionNameType2 != null
            || optionNameType3 != null)) {
            throw new ProductException(ErrorCode.PRODUCT_USE_SINGLE_OPTION);
        }
        this.optionNameType1 = optionNameType1;
        this.optionNameType2 = optionNameType2;
        this.optionNameType3 = optionNameType3;
        this.size = getSize(optionNameType2, optionNameType3);
    }

    private static int getSize(String optionNameType2,
        String optionNameType3) {
        int result = 1;
        if (optionNameType2 != null) {
            result += 1;
        }
        if (optionNameType3 != null) {
            result += 1;
        }
        return result;
    }
}