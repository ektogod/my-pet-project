package com.tinkoff_lab.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class EmailSenderService {
    JavaMailSender mailSender;
    Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

    public void sendEmails(String email, String text) {
        try {
            logger.info("Start sending message on email {}", email);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ektogod@mail.ru");
            message.setTo(email);
            message.setSubject("Your current weather!");
            message.setText(text);
            mailSender.send(message);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
        logger.info("Message was sent successfully on email {}", email);
    }
}
