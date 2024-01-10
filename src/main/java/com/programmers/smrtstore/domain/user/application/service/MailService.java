package com.programmers.smrtstore.domain.user.application.service;

import static com.programmers.smrtstore.core.properties.ErrorCode.EMAIL_SENDING_ERROR;

import com.programmers.smrtstore.domain.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

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
        message.setText(code + "\n해윙 ㅋㅋㅋㅋㅋㅋ 인증 문자 보내기 성공 ㅋㅅㅋ");

        return message;
    }
}