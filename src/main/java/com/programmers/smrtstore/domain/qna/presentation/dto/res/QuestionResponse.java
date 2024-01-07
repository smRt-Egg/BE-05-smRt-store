package com.programmers.smrtstore.domain.qna.presentation.dto.res;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuestionResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private LocalDateTime createdAt;
    private Boolean isAnswered;

    public QuestionResponse(Long id, Long userId, Long productId, String productName, LocalDateTime createdAt, Boolean isAnswered) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.createdAt = createdAt;
        this.isAnswered = isAnswered;
    }
}
