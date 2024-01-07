package com.programmers.smrtstore.domain.qna.presentation.dto.res;

import com.programmers.smrtstore.domain.qna.domain.entity.ProductQuestion;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateQuestionResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private UpdateQuestionResponse(Long id, Long userId, Long productId, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UpdateQuestionResponse of(ProductQuestion productQuestion) {
        return new UpdateQuestionResponse(
                productQuestion.getId(),
                productQuestion.getUserId(),
                productQuestion.getProductId(),
                productQuestion.getContent(),
                productQuestion.getCreatedAt(),
                productQuestion.getUpdatedAt()
        );
    }
}
