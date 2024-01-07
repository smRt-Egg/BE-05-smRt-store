package com.programmers.smrtstore.domain.qna.presentation.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateQuestionRequest {
    Long userId;
    Long productId;
    String content;
}
