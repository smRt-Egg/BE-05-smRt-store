package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.ALGORITHM_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.DUPLICATE_EMAIL;
import static com.programmers.smrtstore.core.properties.ErrorCode.USER_NOT_FOUND;
import static com.programmers.smrtstore.core.properties.ErrorCode.VERIFICATION_CODE_ERROR;
import static com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse.toDetailUserResponse;

import com.programmers.smrtstore.domain.auth.jwt.JwtToken;
import com.programmers.smrtstore.domain.user.domain.entity.User;
import com.programmers.smrtstore.domain.user.exception.UserException;
import com.programmers.smrtstore.domain.user.infrastructure.UserRepository;
import com.programmers.smrtstore.domain.user.presentation.dto.req.UpdateUserRequest;
import com.programmers.smrtstore.domain.user.presentation.dto.res.DetailUserResponse;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final MailService mailService;
    private static final String VERIFICATION_CODE_PRIFIX = "VerificationCode ";
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

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

    public void sendCodeToEmail(String userEmail) {
        this.checkDuplicatedEmail(userEmail);
        String title = "smRt store 인증 번호";
        String certificationCode = createCode();
        mailService.sendEmail(userEmail, title, certificationCode);
        redisService.setValues(VERIFICATION_CODE_PRIFIX + userEmail,
            certificationCode, Duration.ofMillis(authCodeExpirationMillis));
    }

    private void checkDuplicatedEmail(String userEmail) {
        userRepository.findByEmail(userEmail).ifPresent(e -> {
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

    public void verifyCode(String userEmail, String code) {
        String savedCode = redisService.getValues(VERIFICATION_CODE_PRIFIX + userEmail);
        boolean verifyResult = redisService.checkExistsValue(savedCode) && savedCode.equals(code);
        if(!verifyResult) throw new UserException(VERIFICATION_CODE_ERROR, code);
    }
}
