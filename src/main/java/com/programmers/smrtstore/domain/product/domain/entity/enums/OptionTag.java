package com.programmers.smrtstore.domain.product.domain.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OptionTag {

    CHOICE(1, "선택"),
    COLOR(2, "색상"),
    SIZE(3, "사이즈"),
    ERROR(-1, "ERROR");


    private final Integer id;
    private final String name;


    public static OptionTag findById(Integer id) {
        for (OptionTag optionTag : OptionTag.values()) {
            if (optionTag.getId().equals(id)) {
                return optionTag;
            }
        }
        return ERROR;
    }

}

