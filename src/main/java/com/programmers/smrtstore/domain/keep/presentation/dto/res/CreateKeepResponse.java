package com.programmers.smrtstore.domain.keep.presentation.dto.res;

import com.programmers.smrtstore.domain.keep.domain.entity.Keep;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateKeepResponse {
    Long userId;
    Long productId;
    LocalDateTime createdAt;

    @Builder
    private CreateKeepResponse(Keep keep) {
        this.userId = keep.getUserId();
        this.productId = keep.getProductId();
        this.createdAt = keep.getCreatedAt();
    }

    public static CreateKeepResponse of(Keep keep) {
        return new CreateKeepResponse(keep);
    }

}
