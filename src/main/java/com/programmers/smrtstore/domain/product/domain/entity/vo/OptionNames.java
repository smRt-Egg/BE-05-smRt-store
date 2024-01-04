package com.programmers.smrtstore.domain.product.domain.entity.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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
    private OptionNames(String optionName1, String optionName2, String optionName3) {
        this.optionName1 = optionName1;
        this.optionName2 = optionName2;
        this.optionName3 = optionName3;
    }

    private void updateOptionName1(String optionName) {
        this.optionName1 = optionName;
    }

    private void updateOptionName2(String optionName) {
        this.optionName2 = optionName;
    }

    private void updateOptionName3(String optionName) {
        this.optionName3 = optionName;
    }

    public void updateOptionNames(OptionNames optionNames) {
        if (optionNames.getOptionName1() != null) {
            updateOptionName1(optionNames.getOptionName1());
        }
        if (optionNames.getOptionName2() != null) {
            updateOptionName2(optionNames.getOptionName2());
        }
        if (optionNames.getOptionName3() != null) {
            updateOptionName3(optionNames.getOptionName3());
        }
    }

}