package com.programmers.smrtstore.domain.user.application;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.presentation.dto.req.LoginRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.SignUpUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.SignUpUserResponse;
import com.programmers.smrtstore.domain.user.presentation.dto.res.UserResponse;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User login(LoginRequest request);

    Optional<User> findByLoginId(String loginId);

    SignUpUserResponse signUp(SignUpUserRequest request);

    UserResponse findByUserId(Long userId);

    Long updateUser(Long userId, UpdateUserRequest request);

    Long deleteUser(Long userId);
}
