package com.programmers.smrtstore.domain.keep.presentation.dto.res;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import lombok.Getter;

@Getter
public class KeepResponse {
    private Long id;
    private Long userId;
    private Long productId;

    private KeepResponse(Keep keep) {
        this.id = keep.getId();
        this.userId = keep.getUserId();
        this.productId = keep.getProductId();
    }

    public static KeepResponse of(Keep keep) {
        return new KeepResponse(keep);
    }
}
