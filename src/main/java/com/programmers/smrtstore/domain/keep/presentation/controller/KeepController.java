package com.programmers.smrtstore.domain.keep.presentation.controller;

import com.programmers.smrtstore.domain.keep.application.KeepService;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.CreateKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.DeleteKeepRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.req.FindKeepByCategoryRequest;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.CreateKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.DeleteKeepResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepRankingResponse;
import com.programmers.smrtstore.domain.keep.presentation.dto.res.KeepResponse;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/keep")
@RequiredArgsConstructor
public class KeepController {
    private final KeepService keepService;

    @PostMapping
    public ResponseEntity<CreateKeepResponse> createKeep(@RequestBody CreateKeepRequest request) {
        CreateKeepResponse response = keepService.createKeep(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<KeepResponse>> getAllKeepsByUserId(@PathVariable Long userId) {
        List<KeepResponse> response = keepService.getAllKeepsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<DeleteKeepResponse> deleteKeep(@RequestBody DeleteKeepRequest request) {
        DeleteKeepResponse response = keepService.deleteKeep(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<KeepResponse>> getAllKeepsByUserIdAndCategory(@RequestBody FindKeepByCategoryRequest request) {
        List<KeepResponse> response = keepService.findKeepByUserAndCategory(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rank")
    public ResponseEntity<List<KeepRankingResponse>> getKeepRanking(@RequestParam @Positive Integer size) {
        List<KeepRankingResponse> response = keepService.getKeepRanking(size);
        return ResponseEntity.ok(response);
    }

}
