/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.service.EmailService
 *  jakarta.mail.MessagingException
 *  jakarta.mail.internet.MimeMessage
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.mail.SimpleMailMessage
 *  org.springframework.mail.javamail.JavaMailSender
 *  org.springframework.mail.javamail.MimeMessageHelper
 *  org.springframework.stereotype.Service
 */
package com.neel.Teacher.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Year;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAssignmentNotification(String toEmail, String studentName, String className, String section, String subject, String title, String dueDate, String teacherName) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("New Assignment: " + subject + " - " + title);
            String emailBody = "<html><head>  <style>    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }    .container { width: 100%; max-width: 600px; margin: 0 auto; }    .header { background-color: #4b7bec; color: white; padding: 20px; text-align: center; }    .content { padding: 20px; }    .footer { background-color: #f5f6fa; padding: 15px; text-align: center; font-size: 12px; color: #666; }    .details { background-color: #f1f2f6; padding: 15px; margin: 15px 0; border-left: 4px solid #4b7bec; }    .button { display: inline-block; background-color: #4b7bec; color: white; padding: 10px 20px;       text-decoration: none; border-radius: 5px; margin-top: 15px; }  </style></head><body>  <div class='container'>    <div class='header'>      <h2>New Assignment Notification</h2>    </div>    <div class='content'>      <p>Dear <strong>" + studentName + "</strong>,</p>      <p>A new assignment has been posted for your class. Please find the details below:</p>      <div class='details'>        <p><strong>Class:</strong> " + className + " " + section + "</p>        <p><strong>Subject:</strong> " + subject + "</p>        <p><strong>Title:</strong> " + title + "</p>        <p><strong>Due Date:</strong> " + dueDate + "</p>        <p><strong>Teacher:</strong> " + teacherName + "</p>      </div>      <p>Please complete and submit this assignment before the due date. If you have any questions,          please contact your teacher.</p>      <a href='https://edu.vaisacademy.com/student-assignment' class='button'>View Assignment</a>    </div>    <div class='footer'>      <p>This is an automated message. Please do not reply to this email.</p>      <p>&copy; " + Year.now().getValue() + " School Management System. All rights reserved.</p>    </div>  </div></body></html>";
            helper.setText(emailBody, true);
            this.mailSender.send(message);
        }
        catch (MessagingException e) {
            System.out.println("Failed to send HTML email: " + e.getMessage());
            this.sendSimpleTextEmail(toEmail, studentName, className, section, subject, title, dueDate, teacherName);
        }
    }

    private void sendSimpleTextEmail(String toEmail, String studentName, String className, String section, String subject, String title, String dueDate, String teacherName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("New Assignment: " + subject + " - " + title);
        String emailBody = "Dear " + studentName + ",\n\nA new assignment has been posted for your class:\n\nClass: " + className + " " + section + "\nSubject: " + subject + "\nTitle: " + title + "\nDue Date: " + dueDate + "\nTeacher: " + teacherName + "\n\nPlease complete and submit this assignment before the due date. If you have any questions, please contact your teacher.\n\nThis is an automated message. Please do not reply to this email.\n\nRegards,\nSchool Administration";
        message.setText(emailBody);
        this.mailSender.send(message);
    }

    public void sendBulkAssignmentNotifications(List<String> toEmails, List<String> studentNames, String className, String section, String subject, String title, String dueDate, String teacherName) {
        for (int i = 0; i < toEmails.size(); ++i) {
            String studentName = i < studentNames.size() ? studentNames.get(i) : "Student";
            this.sendAssignmentNotification(toEmails.get(i), studentName, className, section, subject, title, dueDate, teacherName);
        }
        System.out.println("Sent assignment notifications to " + toEmails.size() + " students");
    }

    public void sendAssignmentReminderNotification(String toEmail, String studentName, String className, String section, String subject, String title, String dueDate, String teacherName, int daysRemaining) {
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject("REMINDER: Assignment Due Soon - " + subject + " - " + title);
            String urgencyColor = "#4b7bec";
            if (daysRemaining <= 1) {
                urgencyColor = "#eb4d4b";
            } else if (daysRemaining <= 3) {
                urgencyColor = "#f0932b";
            }
            String emailBody = "<!DOCTYPE html><html><head>  <style>    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }    .container { width: 100%; max-width: 600px; margin: 0 auto; }    .header { background-color: " + urgencyColor + "; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }    .content { padding: 20px; border: 1px solid #ddd; border-top: none; }    .footer { background-color: #f5f6fa; padding: 15px; text-align: center; font-size: 12px; color: #666; border-radius: 0 0 5px 5px; }    .details { background-color: #f1f2f6; padding: 15px; margin: 15px 0; border-left: 4px solid " + urgencyColor + "; }    .reminder { font-size: 18px; color: " + urgencyColor + "; font-weight: bold; text-align: center; margin: 15px 0; }    .button { display: inline-block; background-color: " + urgencyColor + "; color: white; padding: 10px 20px;       text-decoration: none; border-radius: 5px; margin-top: 15px; }  </style></head><body>  <div class='container'>    <div class='header'>      <h2>Assignment Reminder</h2>    </div>    <div class='content'>      <p>Dear <strong>" + studentName + "</strong>,</p>      <div class='reminder'>        " + (String)(daysRemaining == 0 ? "Your assignment is due TODAY!" : "Your assignment is due in " + daysRemaining + " day" + (daysRemaining > 1 ? "s" : "") + "!") + "      </div>      <p>This is a reminder about your pending assignment:</p>      <div class='details'>        <p><strong>Class:</strong> " + className + " " + section + "</p>        <p><strong>Subject:</strong> " + subject + "</p>        <p><strong>Title:</strong> " + title + "</p>        <p><strong>Due Date:</strong> " + dueDate + "</p>        <p><strong>Teacher:</strong> " + teacherName + "</p>      </div>      <p>Please ensure you complete and submit this assignment before the deadline.</p>      <a href='https://edu.vaisacademy.com/student-assignment' class='button'>View Assignment</a>    </div>    <div class='footer'>      <p>This is an automated reminder. Please do not reply to this email.</p>      <p>&copy; " + Year.now().getValue() + " School Management System. All rights reserved.</p>    </div>  </div></body></html>";
            helper.setText(emailBody, true);
            this.mailSender.send(message);
        }
        catch (MessagingException e) {
            System.out.println("Failed to send HTML reminder email: " + e.getMessage());
        }
    }
}

