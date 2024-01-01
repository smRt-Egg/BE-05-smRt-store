package com.programmers.smrtstore.domain.user.presentation;

import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<DetailUserResponse> me() {
        DetailUserResponse response = userService.getUserInfo();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<DetailUserResponse> update(@RequestBody
    UpdateUserRequest request) {
        DetailUserResponse response = userService.update(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<DetailUserResponse> withdraw() {
        DetailUserResponse response = userService.withdraw();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verification-request")
    public ResponseEntity<Void> sendMail(@RequestParam("email") @Valid @Email String email) {
        log.info("보내기 전");
        userService.sendCodeToEmail(email);
        log.info("보내기 성공");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/verification")
    public ResponseEntity<String> verify(@RequestParam("email") @Valid @Email String userEmail,
                                        @RequestParam("code") String code) {
        userService.verifyCode(userEmail, code);
        return ResponseEntity.ok("인증에 성공했습니다.");
    }
}
