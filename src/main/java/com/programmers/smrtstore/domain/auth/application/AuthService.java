package com.programmers.smrtstore.domain.auth.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.auth.application.dto.req.SignUpRequest;
import com.programmers.smrtstore.domain.auth.application.dto.res.LoginResponse;
import com.programmers.smrtstore.domain.auth.application.dto.res.SignUpResponse;
import com.programmers.smrtstore.domain.auth.domain.entity.Auth;
import com.programmers.smrtstore.domain.auth.exception.AuthException;
import com.programmers.smrtstore.domain.auth.infrastructure.AuthRepository;
import com.programmers.smrtstore.domain.auth.presentation.dto.req.UpdatePasswordRequest;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public LoginResponse login(String username, String password) {
        Auth auth = authRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));
        auth.verifyPassword(passwordEncoder, password);
        return LoginResponse.from(auth);
    }

    public SignUpResponse signUp(@Valid SignUpRequest request) {
        checkDuplicateUsername(request.getUsername());
        User user = userRepository.save(request.toUserEntity());
        Auth auth = authRepository.save(request.toAuthEntity(user, passwordEncoder));
        return SignUpResponse.from(auth);
    }

    // TODO: reissue
    public void reissue(){

    }

    @Transactional(readOnly = true)
    public void checkDuplicateUsername(String username) {
        authRepository.findByUsername(username).ifPresent(auth -> {
            throw new AuthException(ErrorCode.DUPLICATE_USERNAME);
        });
    }

    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        Auth auth = authRepository.findByUserId(userId).get();
        auth.updatePassword(request.getPassword(), passwordEncoder);
    }
}
