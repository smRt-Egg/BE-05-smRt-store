package com.programmers.smrtstore.domain.qna.infrastructure;

import com.programmers.smrtstore.domain.qna.presentation.dto.res.QuestionResponse;

import java.util.List;

public interface ProductQuestionRepositoryCustom {
    List<QuestionResponse> findByUserId(Long userId);
    List<QuestionResponse> findByProductId(Long productId);
}
