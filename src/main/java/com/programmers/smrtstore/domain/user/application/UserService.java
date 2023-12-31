package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.EMAIL_SENDING_ERROR;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;
import static com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse.toDetailUserResponse;

import com.programmers.smrtstore.domain.auth.jwt.JwtToken;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
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
        JwtToken jwtToken = (JwtToken) authentication.getPrincipal();
        Long userId = jwtToken.getUserId();

        return userRepository.findById(userId)
            .orElseThrow(() -> new UserException(USER_NOT_FOUND, String.valueOf(userId)));
    }


    public void sendCodeToEmail(String mail) {

    }
}
