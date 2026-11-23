/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.kernel.pdf.PdfDocument
 *  com.itextpdf.kernel.pdf.PdfWriter
 *  com.itextpdf.layout.Document
 *  com.itextpdf.layout.element.IBlockElement
 *  com.itextpdf.layout.element.Paragraph
 *  com.neel.RoutineMain.model.Notification
 *  com.neel.RoutineMain.model.NotificationResponse
 *  com.neel.RoutineMain.repo.NotificationRepository
 *  com.neel.RoutineMain.service.NotificationService
 *  jakarta.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.RoutineMain.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.neel.RoutineMain.model.Notification;
import com.neel.RoutineMain.model.NotificationResponse;
import com.neel.RoutineMain.repo.NotificationRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<NotificationResponse> getNotifications() {
        String schoolId = "67ca9167d56ec91fc9539234";
        List notifications = this.notificationRepository.findBySchoolId(schoolId);
        return notifications.stream().map(notification -> {
            boolean isNew = notification.getCreatedAt().isAfter(LocalDateTime.now().minusDays(3L));
            return new NotificationResponse(notification.getId(), notification.getEventName(), notification.getDescription(), notification.getEventDate(), notification.getCreatedAt(), isNew);
        }).sorted((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt())).collect(Collectors.toList());
    }

    public Notification createNotification(Notification notification) {
        notification.setSchoolId("67ca9167d56ec91fc9539234");
        notification.setCreatedAt(LocalDateTime.now());
        return (Notification)this.notificationRepository.save((Object)notification);
    }

    public Notification findNotificationById(String id) {
        return (Notification)this.notificationRepository.findById((Object)id).orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    public void generateNotificationPdf(Notification notification, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=notification.pdf");
        PdfWriter writer = new PdfWriter((OutputStream)response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add((IBlockElement)new Paragraph("Event Name: " + notification.getEventName()));
        document.add((IBlockElement)new Paragraph("Description: " + notification.getDescription()));
        document.add((IBlockElement)new Paragraph("Event Date: " + notification.getEventDate()));
        document.close();
    }

    public void deleteNotification(String id) {
        if (!this.notificationRepository.existsById((Object)id)) {
            throw new NoSuchElementException("Notification not found with ID: " + id);
        }
        this.notificationRepository.deleteById((Object)id);
    }

    public List<NotificationResponse> getNotificationsBySchool(String schoolId) {
        List notifications = this.notificationRepository.findBySchoolId(schoolId);
        return notifications.stream().map(notification -> {
            boolean isNew = notification.getCreatedAt().isAfter(LocalDateTime.now().minusDays(3L));
            return new NotificationResponse(notification.getId(), notification.getEventName(), notification.getDescription(), notification.getEventDate(), notification.getCreatedAt(), isNew);
        }).sorted((n1, n2) -> n2.getCreatedAt().compareTo(n1.getCreatedAt())).collect(Collectors.toList());
    }

    public Notification createNotificationWithSchool(Notification notification, String schoolId) {
        notification.setSchoolId(schoolId);
        notification.setCreatedAt(LocalDateTime.now());
        return (Notification)this.notificationRepository.save((Object)notification);
    }

    public Notification findNotificationByIdAndSchool(String id, String schoolId) {
        return (Notification)this.notificationRepository.findByIdAndSchoolId(id, schoolId).orElseThrow(() -> new RuntimeException("Notification not found or does not belong to this school"));
    }

    public void deleteNotificationWithSchool(String id, String schoolId) {
        if (!this.notificationRepository.findByIdAndSchoolId(id, schoolId).isPresent()) {
            throw new NoSuchElementException("Notification not found with ID: " + id + " for school: " + schoolId);
        }
        this.notificationRepository.deleteById((Object)id);
    }
}

