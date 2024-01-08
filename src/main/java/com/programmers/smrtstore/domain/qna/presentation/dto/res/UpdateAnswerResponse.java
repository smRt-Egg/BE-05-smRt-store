package com.programmers.smrtstore.domain.qna.presentation.dto.res;

import com.programmers.smrtstore.domain.qna.domain.entity.ProductAnswer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateAnswerResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UpdateAnswerResponse(Long id, Long userId, Long productId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UpdateAnswerResponse of(ProductAnswer productAnswer) {
        return new UpdateAnswerResponse(
                productAnswer.getId(),
                productAnswer.getProductQuestion().getUserId(),
                productAnswer.getProductQuestion().getProductId(),
                productAnswer.getContent(),
                productAnswer.getCreatedAt(),
                productAnswer.getUpdatedAt()
        );
    }
}
