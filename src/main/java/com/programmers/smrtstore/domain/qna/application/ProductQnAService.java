package com.programmers.smrtstore.domain.qna.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.qna.domain.entity.ProductAnswer;
import com.programmers.smrtstore.domain.qna.domain.entity.ProductQuestion;
import com.programmers.smrtstore.domain.qna.exception.QnAException;
import com.programmers.smrtstore.domain.qna.infrastructure.ProductAnswerRepository;
import com.programmers.smrtstore.domain.qna.infrastructure.ProductQuestionRepository;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.*;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.*;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductQnAService {
    private final ProductAnswerRepository productAnswerRepository;

    private final ProductQuestionRepository questionRepository;
    private final UserRepository userRepository;

    public CreateQuestionResponse createQuestion(Long userId, CreateQuestionRequest request) {
        checkUserValid(userId, request.getUserId());
        checkUserExist(userId);
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
        checkUserValid(userId, request.getUserId());
        checkUserExist(userId);
        return questionRepository.findByUserId(userId);
    }

    public UpdateQuestionResponse updateQuestion(Long userId, UpdateQuestionRequest request) {
        checkUserExist(userId);
        ProductQuestion productQuestion = questionRepository.findById(request.getId()).orElseThrow(() -> new QnAException(ErrorCode.QUESTION_NOT_FOUND));
        productQuestion.updateContent(request.getContent());
        return UpdateQuestionResponse.of(productQuestion);
    }

    public DeleteQuestionResponse deleteQuestion(Long userId, Long deleteId) {
        checkUserExist(userId);
        ProductQuestion deleteProductQuestion = questionRepository.findById(deleteId).orElseThrow(() -> new QnAException(ErrorCode.QUESTION_NOT_FOUND));
        questionRepository.delete(deleteProductQuestion);
        return DeleteQuestionResponse.of(deleteProductQuestion);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> findByProductId(Long productId) {
        return questionRepository.findByProductId(productId);
    }

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

    public UpdateAnswerResponse updateAnswer(Long userId, UpdateAnswerRequest request) {
        checkUserExist(userId);
        ProductAnswer productAnswer = productAnswerRepository.findById(request.getId()).orElseThrow(() -> new QnAException(ErrorCode.ANSWER_NOT_FOUND));
        productAnswer.updateContent(request.getContent());
        return UpdateAnswerResponse.of(productAnswer);
    }

    void checkUserValid(Long tokenUserId, Long requestUserId) {
        if(!tokenUserId.equals(requestUserId)) {
            throw new QnAException(ErrorCode.USER_NOT_FOUND);
        }
    }

    void checkUserExist(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new QnAException(ErrorCode.USER_NOT_FOUND));
    }
}
