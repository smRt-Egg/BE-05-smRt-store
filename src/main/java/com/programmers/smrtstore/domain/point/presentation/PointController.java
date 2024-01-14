package com.programmers.smrtstore.domain.point.presentation;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.point.application.PointService;
import com.programmers.smrtstore.domain.point.application.dto.req.PointHistoryRequest;
import com.programmers.smrtstore.domain.point.presentation.dto.res.PointHistoryAPIResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/points")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @Secured("ROLE_USER")
    @PostMapping("/history/list")
    public ResponseEntity<List<PointHistoryAPIResponse>> batchPointExpirationProcess(
        @UserId Long securityUserId,
        @Valid @RequestBody PointHistoryRequest request) {

        var result = pointService.getPointHistory(securityUserId, request)
            .stream()
            .map(PointHistoryAPIResponse::from)
            .toList();
        return ResponseEntity.ok(result);
    }
}
