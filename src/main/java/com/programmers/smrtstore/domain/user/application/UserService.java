package com.programmers.smrtstore.domain.user.application;

import static com.google.common.base.Preconditions.checkArgument;
import static io.micrometer.common.util.StringUtils.isNotEmpty;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.LoginRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.UserResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User login(String principal, String credentials) {
        checkArgument(isNotEmpty(principal), "principal must be provided.");
        checkArgument(isNotEmpty(credentials), "credentials must be provided.");

        User user = userRepository.findByLoginId(principal)
            .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + principal));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByLoginId(String loginId) {
        checkArgument(isNotEmpty(loginId), "loginId must be provided.");
        return userRepository.findByLoginId(loginId);
    }

    public SignUpUserResponse signUp(SignUpUserRequest request) {
        return null;
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
