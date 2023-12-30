package com.programmers.smrtstore.domain.user.application;

import static com.programmers.smrtstore.core.properties.ErrorCode.EMAIL_SENDING_ERROR;

import com.programmers.smrtstore.domain.user.exception.UserException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;

@Service
@Transactional
@RequiredArgsConstructor
public class MailService {


    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "sjlim1999@gmail.com";

    public void sendMail(String mail) {
        int certificateNum = createCertificateNum();
        MimeMessage message = createMail(mail, certificateNum);
        javaMailSender.send(message);
    }

    private int createCertificateNum() {
        return (int) (Math.random() * 90000) + 10000;
    }

    private MimeMessage createMail(String mail, int certificateNum) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = makeMessageBody(certificateNum);
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            throw new UserException(EMAIL_SENDING_ERROR, String.valueOf(message));
        }

        return message;
    }

    private String makeMessageBody(int certificateNum) {
        String body = "";
        body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
        body += "<h1>" + certificateNum + "</h1>";
        return body;
    }
}
