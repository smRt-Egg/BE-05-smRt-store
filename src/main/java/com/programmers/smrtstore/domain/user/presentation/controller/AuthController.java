package com.programmers.smrtstore.domain.user.presentation.controller;

import static com.programmers.smrtstore.domain.user.presentation.dto.res.DetailAuthResponse.toDetailUserResponse;

import com.programmers.smrtstore.domain.auth.jwt.JwtAuthentication;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationToken;
import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.presentation.dto.req.LoginRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailAuthResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SignUpUserResponse> signUp(
        @RequestBody @Valid SignUpUserRequest request) {
        SignUpUserResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{loginId}")
    public ResponseEntity<String> checkDuplication(@PathVariable @NotEmpty String loginId) {
        userService.checkDuplicate(loginId);
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<DetailAuthResponse> login(@RequestBody @Valid LoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getPrincipal(),
            request.getCredentials());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        User user = (User) resultToken.getDetails();
        DetailAuthResponse response = toDetailUserResponse(authentication, user);
        return ResponseEntity.ok(response);
    }
}
