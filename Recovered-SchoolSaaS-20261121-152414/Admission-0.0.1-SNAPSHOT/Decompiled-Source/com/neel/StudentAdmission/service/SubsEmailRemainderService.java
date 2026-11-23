/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.service.SubsEmailRemainderService
 *  jakarta.mail.MessagingException
 *  jakarta.mail.internet.MimeMessage
 *  org.springframework.mail.javamail.JavaMailSender
 *  org.springframework.mail.javamail.MimeMessageHelper
 *  org.springframework.stereotype.Service
 */
package com.neel.StudentAdmission.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SubsEmailRemainderService {
    private final JavaMailSender mailSender;

    public SubsEmailRemainderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSubscriptionReminder(String to, String planName, LocalDateTime endDate) {
        MimeMessage message = this.mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject("\ud83d\udcc5 Your Subscription is Expiring Soon \u2013 Take Action Today!");
            String htmlContent = "<html>\n    <head>\n        <style>\n            @media only screen and (max-width: 600px) {\n                .container {\n                    width: 100%% !important;\n                    padding: 20px !important;\n                }\n            }\n        </style>\n    </head>\n    <body style=\"margin: 0; padding: 0; font-family: 'Segoe UI', sans-serif; background-color: #f9f9f9; color: #333;\">\n        <div class=\"container\" style=\"max-width: 600px; margin: 40px auto; background-color: #ffffff; padding: 30px 40px; border-radius: 12px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);\">\n\n            <h2 style=\"color: #2c3e50; margin-bottom: 10px;\">\ud83d\udc4b Hello there!</h2>\n\n            <p style=\"font-size: 17px; line-height: 1.6;\">\n                Just a friendly reminder \u2014 your <strong>%s</strong> subscription is set to\n                <span style=\"color: #e74c3c;\"><strong>expire on %s</strong></span>.\n            </p>\n\n            <p style=\"font-size: 17px; line-height: 1.6;\">\n                To make sure you continue enjoying all the features without any interruptions, we recommend renewing your subscription before it ends.\n            </p>\n\n            <div style=\"text-align: center; margin: 30px 0;\">\n                <a href=\"https://your-website.com/renew\"\n                   style=\"background-color: #4CAF50; color: white; padding: 14px 24px;\n                          text-decoration: none; font-size: 16px; border-radius: 6px; display: inline-block;\">\n                    \ud83d\udd04 Renew Now\n                </a>\n            </div>\n\n            <p style=\"font-size: 15px; color: #666; text-align: center;\">\n                If you've already renewed, you can ignore this message.<br>\n                Need help? Reach out to us anytime at\n                <a href=\"mailto:support@your-website.com\" style=\"color: #007bff;\">support@your-website.com</a>.\n            </p>\n\n            <hr style=\"margin: 30px 0; border: none; border-top: 1px solid #ddd;\">\n\n            <p style=\"font-size: 13px; color: #aaa; text-align: center;\">\n                Thank you for being part of our community. <br>\n                \u2014 The Student Admission Team \ud83d\udc99\n            </p>\n        </div>\n    </body>\n</html>\n\n\n".formatted(planName, endDate.toLocalDate());
            helper.setText(htmlContent, true);
            this.mailSender.send(message);
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

