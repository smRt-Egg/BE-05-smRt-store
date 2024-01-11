package com.programmers.smrtstore.domain.point.presentation.dto.res;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointBenefitsAPIResponse {

    private Integer discountSalePrice;
    private Integer managerPurchasePoint;
    private Integer managerPurchaseExtraPoint;

}
