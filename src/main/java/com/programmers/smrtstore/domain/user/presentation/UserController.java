package com.programmers.smrtstore.domain.user.presentation;

import static com.programmers.smrtstore.core.properties.ErrorCode.DUPLICATE_LOGIN_ID;

import com.programmers.smrtstore.domain.auth.jwt.JwtAuthentication;
import com.programmers.smrtstore.domain.auth.jwt.JwtAuthenticationToken;
import com.programmers.smrtstore.domain.user.application.UserService;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.presentation.dto.req.LoginRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserDto;
import com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/v1/auth/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @GetMapping("/exists/{loginId}")
    public ResponseEntity<String> checkDuplication(@PathVariable String loginId) {
        boolean exist = userService.isExist(loginId);
        if (exist) {
            throw new UserException(DUPLICATE_LOGIN_ID, loginId);
        }
        return ResponseEntity.ok("사용 가능한 아이디입니다.");
    }

    @PostMapping
    public ResponseEntity<SignUpUserResponse> signUp(
        @RequestBody @Valid SignUpUserRequest request) {
        SignUpUserResponse response = userService.signUp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<DetailUserDto> login(@RequestBody @Valid LoginRequest request) {
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(
            request.getPrincipal(), request.getCredentials());
        log.info("authToken : " + authToken.getPrincipal() + " " + authToken.getCredentials());
        Authentication resultToken = authenticationManager.authenticate(authToken);
        log.info("결과 토큰 얻기");
        JwtAuthentication authentication = (JwtAuthentication) resultToken.getPrincipal();
        log.info("authentication");
        User user = (User) resultToken.getDetails();
        DetailUserDto response = new DetailUserDto(authentication.getAccessToken(),
            authentication.getUsername(), authentication.getRefreshToken(), user.getRole());
        return ResponseEntity.ok(response);
    }
}
