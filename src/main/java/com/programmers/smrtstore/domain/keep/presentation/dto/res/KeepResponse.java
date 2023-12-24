package com.programmers.smrtstore.domain.keep.presentation.dto.res;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import lombok.Getter;

@Getter
public class KeepResponse {
    private Long id;
    private Long userId;
    private Long productId;

    private KeepResponse(Long id, Long userId, Long productId) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
    }

    public static KeepResponse of(Keep keep) {
        return new KeepResponse(
            keep.getId(),
            keep.getUserId(),
            keep.getProductId()
        );
    }
}
