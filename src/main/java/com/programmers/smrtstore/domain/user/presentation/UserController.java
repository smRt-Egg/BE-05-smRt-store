package com.programmers.smrtstore.domain.user.presentation;

import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileUserResponse> profile(@RequestAttribute(value = "userId") Long userId) {
        ProfileUserResponse response = userService.getUserInfo(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ProfileUserResponse> update(@RequestAttribute(value = "userId") Long userId, @RequestBody
    UpdateUserRequest request) {
        ProfileUserResponse response = userService.update(userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<ProfileUserResponse> withdraw(Long userId) {
        ProfileUserResponse response = userService.withdraw(userId);
        return ResponseEntity.ok(response);
    }
}
