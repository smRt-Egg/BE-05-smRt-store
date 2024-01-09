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
        this.size = getSize(optionNameType1, optionNameType2, optionNameType3);
        validateOptionNameTypes(size, optionNameType1, optionNameType2, optionNameType3);
        this.optionNameType1 = optionNameType1;
        this.optionNameType2 = optionNameType2;
        this.optionNameType3 = optionNameType3;
    }

    private static int getSize(String optionNameType1, String optionNameType2,
        String optionNameType3) {
        return Stream.of(optionNameType1, optionNameType2, optionNameType3)
            .filter(Objects::nonNull)
            .filter(optionType -> !optionType.isBlank())
            .mapToInt(option -> 1)
            .sum();
    }

    private static void validateOptionNameTypes(int size, String optionNameType1,
        String optionNameType2, String optionNameType3) {
        if (size == 1) {
            if (optionNameType1 == null || optionNameType1.isBlank()) {
                throw new ProductException(ErrorCode.PRODUCT_OPTION_NAME_TYPE_INVALID);
            }
        } else if (size == 2) {
            if (optionNameType2 == null || optionNameType2.isBlank() || optionNameType2.equals(
                optionNameType1)) {
                throw new ProductException(ErrorCode.PRODUCT_OPTION_NAME_TYPE_INVALID);
            }
        } else if (size == 3 && (optionNameType3 == null || optionNameType3.isBlank()
            || optionNameType3.equals(
            optionNameType1) || optionNameType3.equals(optionNameType2))) {
            throw new ProductException(ErrorCode.PRODUCT_OPTION_NAME_TYPE_INVALID);
        }
    }

    public void updateOptionNameTypes(String optionNameType1, String optionNameType2,
        String optionNameType3){
        validateOptionNameTypes(size, optionNameType1, optionNameType2, optionNameType3);
        this.optionNameType1 = optionNameType1;
        this.optionNameType2 = optionNameType2;
        this.optionNameType3 = optionNameType3;
    }
}