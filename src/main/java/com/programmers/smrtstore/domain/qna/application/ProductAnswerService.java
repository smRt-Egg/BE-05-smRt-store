package com.programmers.smrtstore.domain.qna.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.qna.domain.entity.ProductAnswer;
import com.programmers.smrtstore.domain.qna.domain.entity.ProductQuestion;
import com.programmers.smrtstore.domain.qna.exception.QnAException;
import com.programmers.smrtstore.domain.qna.infrastructure.ProductAnswerRepository;
import com.programmers.smrtstore.domain.qna.infrastructure.ProductQuestionRepository;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.CreateAnswerRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.UpdateAnswerRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.AnswerResponse;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.UpdateAnswerResponse;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ProductAnswerService {
    private final ProductAnswerRepository answerRepository;
    private final ProductQuestionRepository questionRepository;
    private final UserJpaRepository userJpaRepository;

    @Secured({ "ROLE_ADMIN" })
    public AnswerResponse addAnswer(Long userId, CreateAnswerRequest request) {
        checkUserExist(userId);
        Long questionId = request.getQuestionId();
        String content = request.getContent();
        ProductQuestion productQuestion = questionRepository.findById(questionId).orElseThrow(() -> new QnAException(ErrorCode.QUESTION_NOT_FOUND));
        ProductAnswer productAnswer = ProductAnswer.builder()
                .productQuestion(productQuestion)
                .content(content)
                .build();
        return AnswerResponse.of(productAnswer);
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> getAnswersByQuestionId(Long userId, Long questionId) {
        ProductQuestion productQuestion = questionRepository.findById(questionId).orElseThrow();
        return productQuestion.getProductAnswerList()
                .stream()
                .map(AnswerResponse::of)
                .toList();
    }

    @Secured({ "ROLE_ADMIN" })
    public UpdateAnswerResponse updateAnswer(Long userId, UpdateAnswerRequest request) {
        checkUserExist(userId);
        ProductAnswer productAnswer = answerRepository.findById(request.getId()).orElseThrow(() -> new QnAException(ErrorCode.ANSWER_NOT_FOUND));
        productAnswer.updateContent(request.getContent());
        return UpdateAnswerResponse.of(productAnswer);
    }

    private void checkUserExist(Long userId) {
        userJpaRepository.findById(userId).orElseThrow(() -> new QnAException(ErrorCode.USER_NOT_FOUND));
    }

}
