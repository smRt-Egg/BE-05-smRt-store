package com.programmers.smrtstore.domain.user.presentation;

import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/email/request")
    public ResponseEntity<Void> sendMail(@RequestParam("email") @Valid @Email String mail) {
        userService.sendCodeToEmail(mail);
        return ResponseEntity.ok().build();
    }
}
