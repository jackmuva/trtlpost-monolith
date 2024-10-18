package com.jackmu.service.security;

import com.jackmu.model.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Profile("!local-profile")
public class AuthEmailServiceAws implements AuthEmailService {
    @Autowired
    private JavaMailSender mailSender;
    private static final String PASSWORD_RESET_ENDPOINT = "https://trtlpost.com/changePassword/";

    @Override
    public void sendResetToken(String token, User user) {
        mailSender.send(constructEmail(PASSWORD_RESET_ENDPOINT + token, user));
    }

    private MimeMessage constructEmail(String body, User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setBcc(user.getEmail());
            helper.setFrom("trtlpost@trtlpost.com", "Trtlpost");
            helper.setSubject("TrtlPost Account Password Recovery");
            helper.setText("<a href = \"" + body + "\">" + body + "</a>", true);
            return message;
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
