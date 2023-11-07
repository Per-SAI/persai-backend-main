package com.exe.persai.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void send(String from, String to, String subject, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setTo(to);
            mailSender.send(mimeMessage);
            log.info("Send mail to " + to + " successfully");
        }
        catch(Exception e) {
            log.error("Failed to send email to " + to);
            throw new IllegalStateException("Failed to send email to " + to);
        }
    }
}
