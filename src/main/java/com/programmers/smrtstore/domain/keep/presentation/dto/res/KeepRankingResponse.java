package com.programmers.smrtstore.domain.keep.presentation.dto.res;

import lombok.Getter;

@Getter
public class KeepRankingResponse {
    private Long productId;
    private Long count;

    public KeepRankingResponse(Long productId, Long count) {
        this.productId = productId;
        this.count = count;
    }
}
