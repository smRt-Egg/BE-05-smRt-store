package com.programmers.smrtstore.domain.product.domain.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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

    @Column(name = "option_name_type_1", length = 50)
    private String optionNameType1;

    @Column(name = "option_name_type_2", length = 50)
    private String optionNameType2;

    @Column(name = "option_name_type_3", length = 50)
    private String optionNameType3;

    @Column(name = "size")
    @JdbcTypeCode(SqlTypes.TINYINT)
    private Integer size;

    @Builder
    private OptionNameTypes(String optionNameType1, String optionNameType2,
        String optionNameType3) {
        this.optionNameType1 = optionNameType1;
        this.optionNameType2 = optionNameType2;
        this.optionNameType3 = optionNameType3;
        this.size = getSize(optionNameType1, optionNameType2, optionNameType3);
    }

    private static int getSize(String optionNameType1, String optionNameType2,
        String optionNameType3) {
        int result = 0;
        if (optionNameType1 != null) {
            result += 1;
        }
        if (optionNameType2 != null) {
            result += 1;
        }
        if (optionNameType3 != null) {
            result += 1;
        }
        return result;
    }
}