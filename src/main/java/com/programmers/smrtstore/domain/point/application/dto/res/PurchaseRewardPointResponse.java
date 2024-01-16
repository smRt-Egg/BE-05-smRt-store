package com.programmers.smrtstore.domain.point.application.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PurchaseRewardPointResponse {

    private final Long orderedProductId;
    private final Integer purchaseRewardPointByOrderedProductId;

    @Builder
    private PurchaseRewardPointResponse(
            Long orderedProductId, Integer purchaseRewardPointByOrderedProductId) {
        this.orderedProductId = orderedProductId;
        this.purchaseRewardPointByOrderedProductId = purchaseRewardPointByOrderedProductId;
    }

    public static PurchaseRewardPointResponse of(
            Long orderedProductId, Integer purchaseRewardPointByOrderedProductId
    ) {
        return PurchaseRewardPointResponse.builder()
            .orderedProductId(orderedProductId)
            .purchaseRewardPointByOrderedProductId(purchaseRewardPointByOrderedProductId)
            .build();
    }
}
