package com.programmers.smrtstore.domain.point.application.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderExpectedPointDto {

    private final Integer defaultPoint;
    private final Integer additionalPoint;
    private final Integer totalPoint;

    @Builder
    private OrderExpectedPointDto(Integer defaultPoint, Integer additionalPoint, Integer totalPoint) {
        this.defaultPoint = defaultPoint;
        this.additionalPoint = additionalPoint;
        this.totalPoint = totalPoint;
    }

    public static OrderExpectedPointDto of(Integer defaultPoint, Integer additionalPoint, Integer totalPoint) {
        return OrderExpectedPointDto.builder()
            .defaultPoint(defaultPoint)
            .additionalPoint(additionalPoint)
            .totalPoint(totalPoint)
            .build();
    }
}
