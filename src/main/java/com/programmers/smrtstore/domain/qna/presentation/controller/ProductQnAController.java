package com.programmers.smrtstore.domain.qna.presentation.controller;

import com.programmers.smrtstore.domain.qna.application.ProductQnAService;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.CreateAnswerRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.CreateQuestionRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.FindQuestionRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.req.UpdateQuestionRequest;
import com.programmers.smrtstore.domain.qna.presentation.dto.res.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductQnAController {
    private final ProductQnAService qnAService;

    @PostMapping("/qna")
    public ResponseEntity<CreateQuestionResponse> createQuestion(@RequestAttribute(value = "userId") Long userId,
                                                                 @RequestBody CreateQuestionRequest request) {
        CreateQuestionResponse response = qnAService.createQuestion(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/qna")
    public ResponseEntity<List<QuestionResponse>> findMyQuestion(@RequestAttribute(value = "userId") Long userId,
                                                                 @RequestBody FindQuestionRequest request) {
        List<QuestionResponse> response = qnAService.findByUserId(userId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/qna")
    public ResponseEntity<UpdateQuestionResponse> updateQuestion(@RequestAttribute(value = "userId") Long userId,
                                                                 @RequestBody UpdateQuestionRequest request) {
        UpdateQuestionResponse response = qnAService.updateQuestion(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/qna/{id}")
    public ResponseEntity<DeleteQuestionResponse> deleteQuestion(@RequestAttribute(value = "userId") Long userId,
                                                                 @PathVariable("id") Long deleteId) {
        DeleteQuestionResponse response = qnAService.deleteQuestion(userId, deleteId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{productId}/qna")
    public ResponseEntity<List<QuestionResponse>> getQuestionByProductId(@RequestAttribute(value = "userId") Long userId,
                                                                         @PathVariable("productId") Long productId) {
        List<QuestionResponse> response = qnAService.findByProductId(userId, productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/qna")
    public ResponseEntity<AnswerResponse> addAnswerToQuestion(@RequestAttribute(value = "userId") Long userId,
                                                              @RequestBody CreateAnswerRequest request) {
        AnswerResponse response = qnAService.addAnswer(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/qna/replies/{questionId}")
    public ResponseEntity<List<AnswerResponse>> getAnswersByQuestionId(@RequestAttribute(value = "userId") Long userId,
                                                                       @PathVariable Long questionId) {
        List<AnswerResponse> response = qnAService.getAnswersByQuestionId(userId, questionId);
        return ResponseEntity.ok(response);
    }
}
