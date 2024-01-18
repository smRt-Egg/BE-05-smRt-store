package com.programmers.smrtstore.domain.user.application.service;

import static com.programmers.smrtstore.core.properties.ErrorCode.EMAIL_SENDING_ERROR;

import com.programmers.smrtstore.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private static final String MESSAGE_BODY = "\n인증번호를 입력해주세요.";

    public void sendEmail(String userEmail, String title, String certificationCode) {
        SimpleMailMessage message = createEmailMessage(userEmail, title, certificationCode);
        try {
            javaMailSender.send(message);
        } catch (RuntimeException e) {
            throw new UserException(EMAIL_SENDING_ERROR, userEmail);
        }
    }

    private SimpleMailMessage createEmailMessage(String userEmail, String title, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(userEmail);
        message.setSubject(title);
        message.setText(code + MESSAGE_BODY);

        return message;
    }
}