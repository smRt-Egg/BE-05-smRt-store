package com.programmers.smrtstore.domain.user.application;

import static com.google.common.base.Preconditions.checkArgument;
import static com.programmers.smrtstore.core.properties.ErrorCode.*;
import static com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest.*;
import static com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse.*;
import static io.micrometer.common.util.StringUtils.isNotEmpty;

import com.programmers.smrtstore.core.properties.ErrorCode;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.UserResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User login(String principal, String credentials) {
        User user = userRepository.findByAuth_LoginId(principal)
            .orElseThrow(
                () -> new UserException(NOT_FOUND_USER, principal));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLoginId(String loginId) {
        return userRepository.findByAuth_LoginId(loginId);
    }

    @Transactional
    public SignUpUserResponse signUp(SignUpUserRequest request) {
        User user = toUser(request);
        User saved = userRepository.save(user);
        return toSignUpUserResponse(saved);
    }

    @Transactional(readOnly = true)
    public boolean isExist(String loginId) {
        return userRepository.findByAuth_LoginId(loginId).isPresent();
    }

    public UserResponse findByUserId(Long userId) {
        return null;
    }

    public Long updateUser(Long userId, UpdateUserRequest request) {
        return null;
    }

    public Long deleteUser(Long userId) {
        return null;
    }
}
