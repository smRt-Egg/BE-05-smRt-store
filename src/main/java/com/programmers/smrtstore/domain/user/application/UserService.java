package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.DUPLICATE_LOGIN_ID;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;
import static com.programmers.smrtstore.domain.user.domain.entity.User.toUser;
import static com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse.toDetailUserResponse;
import static com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse.toSignUpUserResponse;

import com.programmers.smrtstore.domain.auth.jwt.JwtAuthentication;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User login(String principal, String credentials) {
        User user = userRepository.findByAuth_LoginId(principal)
            .orElseThrow(
                () -> new UserException(USER_NOT_FOUND, principal));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional(readOnly = true)
    public void checkDuplicate(String loginId) {
        userRepository.findByAuth_LoginId(loginId)
            .orElseThrow(() -> new UserException(DUPLICATE_LOGIN_ID, loginId));
    }

    @Transactional(readOnly = true)
    public DetailUserResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();
        String loginId = jwtAuthentication.getUsername();

        User user = userRepository.findByAuth_LoginId(loginId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, loginId));
        return toDetailUserResponse(user);
    }

    public SignUpUserResponse signUp(SignUpUserRequest request) {
        User user = toUser(request, passwordEncoder);
        User saved = userRepository.save(user);
        return toSignUpUserResponse(saved);
    }

    @Transactional(readOnly = true)
    public DetailUserResponse findByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
        return toDetailUserResponse(user);
    }

    public Long updateUser(Long userId, UpdateUserRequest request) {
        return null;
    }

    public Long deleteUser(Long userId) {
        return null;
    }
}
