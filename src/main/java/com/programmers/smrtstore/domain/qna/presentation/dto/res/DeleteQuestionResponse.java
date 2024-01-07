package com.programmers.smrtstore.domain.qna.presentation.dto.res;

import com.programmers.smrtstore.domain.qna.domain.entity.ProductQuestion;
import lombok.Getter;

@Getter
public class DeleteQuestionResponse {
    private Long id;

    private DeleteQuestionResponse(Long id) {
        this.id = id;
    }

    public static DeleteQuestionResponse of(ProductQuestion productQuestion) {
        return new DeleteQuestionResponse(productQuestion.getId());
    }
}
