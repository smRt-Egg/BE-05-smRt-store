package com.programmers.smrtstore.domain.qna.presentation.controller;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.qna.application.ProductAnswerService;
import com.programmers.smrtstore.domain.qna.application.ProductQuestionService;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.*;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductQnAController {
    private final ProductQuestionService questionService;
    private final ProductAnswerService answerService;

    @PostMapping("/qna")
    public ResponseEntity<CreateQuestionResponse> createQuestion(@UserId Long userId,
                                                                 @RequestBody CreateQuestionRequest request) {
        CreateQuestionResponse response = questionService.createQuestion(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/qna")
    public ResponseEntity<List<QuestionResponse>> findMyQuestion(@UserId Long userId,
                                                                 @RequestBody FindQuestionRequest request) {
        List<QuestionResponse> response = questionService.findByUserId(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/qna")
    public ResponseEntity<UpdateQuestionResponse> updateQuestion(@UserId Long userId,
                                                                 @RequestBody UpdateQuestionRequest request) {
        UpdateQuestionResponse response = questionService.updateQuestion(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/qna/{id}")
    public ResponseEntity<DeleteQuestionResponse> deleteQuestion(@UserId Long userId,
                                                                 @PathVariable("id") Long deleteId) {
        DeleteQuestionResponse response = questionService.deleteQuestion(userId, deleteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{productId}/qna")
    public ResponseEntity<List<QuestionResponse>> getQuestionByProductId(@PathVariable("productId") Long productId) {
        List<QuestionResponse> response = questionService.findByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/qna")
    public ResponseEntity<AnswerResponse> addAnswerToQuestion(@UserId Long userId,
                                                              @RequestBody CreateAnswerRequest request) {
        AnswerResponse response = answerService.addAnswer(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/qna/reply")
    public ResponseEntity<UpdateAnswerResponse> updateAnswer(@UserId Long userId,
                                                             @RequestBody UpdateAnswerRequest request) {
        UpdateAnswerResponse response = answerService.updateAnswer(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/qna/reply/{questionId}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByQuestionId(@UserId Long userId,
                                                                       @PathVariable Long questionId) {
        List<AnswerResponse> response = answerService.getAnswersByQuestionId(userId, questionId);
        return ResponseEntity.ok(response);
    }
}
