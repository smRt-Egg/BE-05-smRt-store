package com.programmers.smrtstore.domain.review.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReviewScore {
    ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5);

    private final int score;
}
