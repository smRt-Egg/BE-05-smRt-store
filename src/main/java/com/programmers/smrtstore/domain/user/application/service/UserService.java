package com.programmers.smrtstore.domain.user.application.service;

import static com.programmers.smrtstore.core.properties.ErrorCode.ALGORITHM_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.DUPLICATE_EMAIL;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.EMAIL_VERIFICATION_CODE_ERROR;

import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserJpaRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.ProfileUserResponse;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final RedisService redisService;
    private final MailService mailService;
    private static final String VERIFICATION_CODE_PRIFIX = "VerificationCode ";
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Transactional(readOnly = true)
    public ProfileUserResponse getUserInfo(Long userId) {
        User user = findByUserId(userId);
        return ProfileUserResponse.from(user);
    }

    private User findByUserId(Long userId) {
        return userJpaRepository.findById(userId)
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

    public String sendCodeToEmail(String userEmail) {
        this.checkDuplicatedEmail(userEmail);
        String title = "smRt store 인증 번호";
        String certificationCode = createCode();
        mailService.sendEmail(userEmail, title, certificationCode);
        redisService.setValues(VERIFICATION_CODE_PRIFIX + userEmail,
            certificationCode, Duration.ofMillis(authCodeExpirationMillis));
        return certificationCode;
    }

    @Transactional(readOnly = true)
    public void verifyCode(String userEmail, String code) {
        String savedCode = redisService.getValues(VERIFICATION_CODE_PRIFIX + userEmail);
        boolean verifyResult = redisService.checkExistsValue(savedCode) && savedCode.equals(code);
        if (!verifyResult) {
            throw new UserException(EMAIL_VERIFICATION_CODE_ERROR, code);
        }
    }

    private void checkDuplicatedEmail(String userEmail) {
        userJpaRepository.findByEmail(userEmail).ifPresent(e -> {
            throw new UserException(DUPLICATE_EMAIL, userEmail);
        });
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new UserException(ALGORITHM_NOT_FOUND);
        }
    }
}
