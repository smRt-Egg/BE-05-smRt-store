package com.programmers.smrtstore.domain.user.presentation.controller;

import static com.programmers.smrtstore.core.properties.ErrorCode.DUPLICATE_LOGIN_ID;

import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/exists/{loginId}")
    public ResponseEntity<String> checkDuplication(@PathVariable String loginId) {
        userService.findByLoginId(loginId)
            .orElseThrow(() -> new UserException(DUPLICATE_LOGIN_ID, loginId));
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping
    public ResponseEntity<SignUpUserResponse> signUp(
        @RequestBody @Valid SignUpUserRequest request) {
        SignUpUserResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }
}
