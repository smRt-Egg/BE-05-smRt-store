package com.programmers.smrtstore.domain.auth.presentation;

import com.programmers.smrtstore.common.annotation.UserId;
import com.programmers.smrtstore.domain.auth.application.AuthService;
import com.programmers.smrtstore.domain.auth.application.dto.req.SignUpRequest;
import com.programmers.smrtstore.domain.auth.application.dto.res.LoginResponse;
import com.programmers.smrtstore.domain.auth.application.dto.res.ReissueResponse;
import com.programmers.smrtstore.domain.auth.application.dto.res.SignUpResponse;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationContext;
import com.programmers.smrtstore.domain.auth.jwt.JwtToken;
import com.programmers.smrtstore.domain.auth.presentation.dto.req.LoginAPIRequest;
import com.programmers.smrtstore.domain.auth.presentation.dto.req.ReissueAPIRequest;
import com.programmers.smrtstore.domain.auth.presentation.dto.req.SignUpAPIRequest;
import com.programmers.smrtstore.domain.auth.presentation.dto.req.UpdatePasswordRequest;
import com.programmers.smrtstore.domain.auth.presentation.dto.res.DetailAuthAPIResponse;
import com.programmers.smrtstore.domain.auth.presentation.dto.res.SignUpAPIResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<SignUpAPIResponse> signUp(
        @RequestBody @Valid SignUpAPIRequest request) {
        SignUpResponse response = authService.signUp(SignUpRequest.from(request));
        return ResponseEntity.ok(SignUpAPIResponse.from(response));
    }

    @GetMapping("/exists/{username}")
    public ResponseEntity<String> checkDuplication(@PathVariable @NotEmpty String username) {
        authService.checkDuplicateUsername(username);
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<DetailAuthAPIResponse> login(
        @RequestBody @Valid LoginAPIRequest request) {
        JwtAuthenticationContext authToken = new JwtAuthenticationContext(request.getUsername(),
            request.getPassword());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        JwtToken authentication = (JwtToken) resultToken.getPrincipal();
        LoginResponse response = (LoginResponse) resultToken.getDetails();
        return ResponseEntity.ok(DetailAuthAPIResponse.of(response, authentication));
    }

    @PostMapping("/reissue")
    public ResponseEntity<DetailAuthAPIResponse> reissue(
        @RequestBody @Valid ReissueAPIRequest request) {
        ReissueResponse response = authService.reissue(request.getUsername(),
            request.getRefreshToken());
        return ResponseEntity.ok(DetailAuthAPIResponse.from(response));
    }

    @PostMapping("/password")
    public ResponseEntity<Void> updatePassword(@UserId Long userId,
        @RequestBody @Valid UpdatePasswordRequest request) {
        authService.updatePassword(userId, request);
        return ResponseEntity.noContent().build();
    }
}
