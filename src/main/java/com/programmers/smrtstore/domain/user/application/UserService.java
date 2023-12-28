package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;
import static com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse.toDetailUserResponse;

import com.programmers.smrtstore.domain.auth.jwt.JwtAuthentication;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public DetailUserResponse getUserInfo() {
        User user = certificatedUser();
        return toDetailUserResponse(user);
    }

    public DetailUserResponse update(UpdateUserRequest request) {
        User user = certificatedUser();
        user.updateUser(request.getAge(), request.getNickName(), request.getEmail(),
            request.getPhone(), request.getBirth(), request.getGender(), request.getThumbnail(),
            request.isMarketingAgree());
        return toDetailUserResponse(user);
    }

    public DetailUserResponse withdraw() {
        User user = certificatedUser();
        user.saveDeleteDate(LocalDateTime.now());
        return toDetailUserResponse(user);
    }

    private User certificatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication.getPrincipal();
        String loginId = jwtAuthentication.getUsername();

        return userRepository.findByAuth_LoginId(loginId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, loginId));
    }
}
