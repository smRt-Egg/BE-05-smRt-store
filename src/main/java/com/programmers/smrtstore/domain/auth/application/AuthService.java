package com.programmers.smrtstore.domain.auth.application;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.auth.application.dto.req.SignUpRequest;
import com.programmers.smrtstore.domain.auth.application.dto.res.LoginResponse;
import com.programmers.smrtstore.domain.auth.application.dto.res.ReissueResponse;
import com.programmers.smrtstore.domain.auth.application.dto.res.SignUpResponse;
import com.programmers.smrtstore.domain.auth.domain.entity.Auth;
import com.programmers.smrtstore.domain.auth.domain.entity.TokenEntity;
import com.programmers.smrtstore.domain.auth.exception.AuthException;
import com.programmers.smrtstore.domain.auth.infrastructure.AuthJPARepository;
import com.programmers.smrtstore.domain.auth.infrastructure.TokenEntityJPARepository;
import com.programmers.smrtstore.domain.auth.jwt.JwtHelper;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthJPARepository authJPARepository;
    private final UserRepository userRepository;
    private final TokenEntityJPARepository tokenEntityJPARepository;
    private final JwtHelper jwtHelper;

    @Transactional(readOnly = true)
    public LoginResponse login(String username, String password) {
        Auth auth = authJPARepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));
        auth.verifyPassword(passwordEncoder, password);
        return LoginResponse.from(auth);
    }

    public SignUpResponse signUp(SignUpRequest request) {
        checkDuplicateUsername(request.getUsername());
        User user = userRepository.save(request.toUserEntity());
        Auth auth = authJPARepository.save(request.toAuthEntity(user, passwordEncoder));
        return SignUpResponse.from(auth);
    }

    public ReissueResponse reissue(String username, String refreshToken) {
        jwtHelper.verify(refreshToken);
        TokenEntity tokenEntity = tokenEntityJPARepository.findByUsernameAndRefreshToken(username,
                refreshToken)
            .orElseThrow(() -> new AuthException(ErrorCode.TOKEN_NOT_FOUND));
        Auth auth = tokenEntity.getAuth();
        var token = jwtHelper.sign(auth.getUser().getId(),
            new String[]{auth.getUser().getRole().name()});
        tokenEntity.updateRefreshToken(token.getRefreshToken(), token.getRefreshTokenExpiryDate());
        return ReissueResponse.of(token, auth.getUser().getRole());
    }


    @Transactional(readOnly = true)
    public void checkDuplicateUsername(String username) {
        authJPARepository.findByUsername(username).ifPresent(auth -> {
            throw new AuthException(ErrorCode.DUPLICATE_USERNAME);
        });
    }
}
