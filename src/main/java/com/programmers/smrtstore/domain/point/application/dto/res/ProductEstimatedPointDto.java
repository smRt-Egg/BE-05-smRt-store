package com.programmers.smrtstore.domain.point.application.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductEstimatedPointDto {

    private final Integer defaultPoint;
    private final Integer additionalPoint;
    private final Integer totalPoint;

    @Builder
    private ProductEstimatedPointDto(Integer defaultPoint, Integer additionalPoint,
        Integer totalPoint) {
        this.defaultPoint = defaultPoint;
        this.additionalPoint = additionalPoint;
        this.totalPoint = totalPoint;
    }

    public static ProductEstimatedPointDto of(Integer defaultPoint, Integer additionalPoint, Integer totalPoint) {
        return ProductEstimatedPointDto.builder()
            .defaultPoint(defaultPoint)
            .additionalPoint(additionalPoint)
            .totalPoint(totalPoint)
            .build();
    }
}
