package com.programmers.smrtstore.domain.product.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    TEMP(1, "TEMP");


    private final Integer id;
    private final String name;
}
