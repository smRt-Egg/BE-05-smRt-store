package com.programmers.smrtstore.domain.qna.presentation.dto.res;

import com.programmers.smrtstore.domain.qna.domain.entity.ProductAnswer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnswerResponse {
    private Long id;
    private Long questionId;
    private String content;
    private LocalDateTime createdAt;

    private AnswerResponse(Long id, Long questionId, String content, LocalDateTime createdAt) {
        this.id = id;
        this.questionId = questionId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static AnswerResponse of (ProductAnswer productAnswer) {
        return new AnswerResponse(
                productAnswer.getId(),
                productAnswer.getProductQuestion().getId(),
                productAnswer.getContent(),
                productAnswer.getCreatedAt()
        );
    }
}
