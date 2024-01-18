package com.programmers.smrtstore.domain.qna.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.qna.domain.entity.ProductQuestion;
import com.programmers.smrtstore.domain.qna.exception.QnAException;
import com.programmers.smrtstore.domain.qna.infrastructure.ProductQuestionRepository;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.*;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.*;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductQuestionService {
    private final ProductQuestionRepository questionRepository;
    private final UserJpaRepository userJpaRepository;

    public CreateQuestionResponse createQuestion(Long userId, CreateQuestionRequest request) {
        checkUserExist(userId);
        checkUserValid(userId, request.getUserId());
        ProductQuestion productQuestion = ProductQuestion.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .content(request.getContent())
                .build();
        questionRepository.save(productQuestion);
        return CreateQuestionResponse.of(productQuestion);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> findByUserId(Long userId, FindQuestionRequest request) {
        checkUserExist(userId);
        checkUserValid(userId, request.getUserId());
        return questionRepository.findByUserId(userId);
    }

    public UpdateQuestionResponse updateQuestion(Long userId, UpdateQuestionRequest request) {
        checkUserExist(userId);
        ProductQuestion productQuestion = questionRepository.findById(request.getId()).orElseThrow(() -> new QnAException(ErrorCode.QUESTION_NOT_FOUND));
        productQuestion.updateContent(userId, request.getContent());
        return UpdateQuestionResponse.of(productQuestion);
    }

    public DeleteQuestionResponse deleteQuestion(Long userId, Long deleteId) {
        checkUserExist(userId);
        ProductQuestion deleteProductQuestion = questionRepository.findById(deleteId).orElseThrow(() -> new QnAException(ErrorCode.QUESTION_NOT_FOUND));
        if (!deleteProductQuestion.checkUserEquals(userId)) {
            throw new QnAException(ErrorCode.QUESTION_ACCESS_DENIED);
        }
        questionRepository.delete(deleteProductQuestion);
        return DeleteQuestionResponse.of(deleteProductQuestion);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> findByProductId(Long productId) {
        return questionRepository.findByProductId(productId);
    }


    private void checkUserValid(Long tokenUserId, Long requestUserId) {
        if(!tokenUserId.equals(requestUserId)) {
            throw new QnAException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private void checkUserExist(Long userId) {
        userJpaRepository.findById(userId).orElseThrow(() -> new QnAException(ErrorCode.USER_NOT_FOUND));
    }
}
