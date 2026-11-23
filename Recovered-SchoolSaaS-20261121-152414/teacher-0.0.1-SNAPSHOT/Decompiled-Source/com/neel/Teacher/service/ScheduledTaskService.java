/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.model.SubjectAssignment
 *  com.neel.Teacher.model.Teachers
 *  com.neel.Teacher.repo.TeachersRepository
 *  com.neel.Teacher.service.ScheduledTaskService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.mail.SimpleMailMessage
 *  org.springframework.mail.javamail.JavaMailSender
 *  org.springframework.scheduling.annotation.Scheduled
 *  org.springframework.stereotype.Service
 */
package com.neel.Teacher.service;

import com.neel.Teacher.model.SubjectAssignment;
import com.neel.Teacher.model.Teachers;
import com.neel.Teacher.repo.TeachersRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTaskService {
    private final TeachersRepository teachersRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public ScheduledTaskService(TeachersRepository teachersRepository, JavaMailSender mailSender) {
        this.teachersRepository = teachersRepository;
        this.mailSender = mailSender;
    }

    @Scheduled(cron="0 0 6 * * ?")
    public void sendDailyAssignmentReminders() {
        LocalDate today = LocalDate.now();
        String dayOfWeek = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        List allTeachers = this.teachersRepository.findAll();
        for (Teachers teacher : allTeachers) {
            if (teacher.getSubjectAssignments() == null || teacher.getSubjectAssignments().isEmpty()) continue;
            StringBuilder messageBody = new StringBuilder();
            boolean hasAssignmentsToday = false;
            messageBody.append("Dear ").append(teacher.getTeachersName()).append(",\n\n");
            messageBody.append("Here is your teaching schedule for today (").append(today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("):\n\n");
            for (SubjectAssignment assignment : teacher.getSubjectAssignments()) {
                if (!assignment.getDayOfWeek().equalsIgnoreCase(dayOfWeek) || !"Active".equalsIgnoreCase(assignment.getStatus())) continue;
                messageBody.append("Subject: ").append(assignment.getSubjectName()).append("\n");
                messageBody.append("Time: ").append(assignment.getTimeSlot()).append("\n");
                messageBody.append("Class: ").append(assignment.getClassGroup()).append("\n\n");
                hasAssignmentsToday = true;
            }
            if (!hasAssignmentsToday) continue;
            messageBody.append("Please ensure you are prepared for these classes.\n\n");
            messageBody.append("Regards,\nSchool Administration");
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(teacher.getEmail());
            message.setSubject("Today's Teaching Schedule - " + today.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            message.setText(messageBody.toString());
            try {
                this.mailSender.send(message);
                System.out.println("Daily reminder sent to: " + teacher.getEmail());
            }
            catch (Exception e) {
                System.out.println("Failed to send daily reminder to " + teacher.getEmail() + ": " + e.getMessage());
            }
        }
    }
}

