package com.programmers.smrtstore.domain.user.presentation.controller;

import static com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse.toDetailUserResponse;

import com.programmers.smrtstore.domain.auth.jwt.JwtAuthentication;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationToken;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.presentation.dto.req.LoginRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<DetailUserResponse> login(@RequestBody @Valid LoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(request.getPrincipal(),
            request.getCredentials());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        User user = (User) resultToken.getDetails();
        DetailUserResponse response = toDetailUserResponse(authentication, user);
        return ResponseEntity.ok(response);
    }
}
