package com.programmers.smrtstore.domain.product.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OptionTag {
    COLOR(1, "색상");

    private final Integer id;
    private final String name;

}

