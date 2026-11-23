/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Auth.service.EmailService
 *  jakarta.mail.MessagingException
 *  jakarta.mail.internet.MimeMessage
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.mail.javamail.JavaMailSender
 *  org.springframework.mail.javamail.MimeMessageHelper
 *  org.springframework.stereotype.Service
 */
package com.neel.Auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("vaisacademy2025@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            this.mailSender.send(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

