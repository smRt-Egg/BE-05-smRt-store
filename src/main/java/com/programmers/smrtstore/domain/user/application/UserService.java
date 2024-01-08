package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProfileUserResponse getUserInfo(Long userId) {
        User user = findByUserId(userId);
        return ProfileUserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }

    public ProfileUserResponse update(Long userId, UpdateUserRequest request) {
        User user = findByUserId(userId);
        user.updateUser(request);
        return ProfileUserResponse.from(user);
    }

    public void withdraw(Long userId) {
        User user = findByUserId(userId);
        user.saveDeleteDate();
    }
}
