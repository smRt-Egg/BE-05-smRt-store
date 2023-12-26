package com.programmers.smrtstore.domain.user.presentation.controller;

import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<DetailUserResponse> findByUserId(@PathVariable Long userId) {
        DetailUserResponse response = userService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<DetailUserResponse> me() {
        DetailUserResponse response = userService.getUserInfo();
        return ResponseEntity.ok(response);
    }
}
