package com.programmers.smrtstore.domain.qna.presentation.dto.res;

import com.programmers.smrtstore.domain.qna.domain.entity.ProductQuestion;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateQuestionResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String content;
    private LocalDateTime createdAt;

    private CreateQuestionResponse(Long id, Long userId, Long productId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static CreateQuestionResponse of(ProductQuestion productQuestion) {
        return new CreateQuestionResponse(
                productQuestion.getId(),
                productQuestion.getUserId(),
                productQuestion.getProductId(),
                productQuestion.getContent(),
                productQuestion.getCreatedAt()
        );
    }
}
