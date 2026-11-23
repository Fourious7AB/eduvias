/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.controller.NotificationController
 *  com.neel.RoutineMain.model.Notification
 *  com.neel.RoutineMain.model.ResponseMessage
 *  com.neel.RoutineMain.service.NotificationService
 *  jakarta.servlet.http.HttpServletResponse
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.RoutineMain.controller;

import com.neel.RoutineMain.model.Notification;
import com.neel.RoutineMain.model.ResponseMessage;
import com.neel.RoutineMain.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api/notifications"})
@CrossOrigin(origins={"*"})
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getNotifications() {
        List notifications = this.notificationService.getNotifications();
        return ResponseEntity.ok((Object)new ResponseMessage("Notifications fetched successfully", (Object)notifications));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createNotification(@RequestBody Notification notification) {
        Notification savedNotification = this.notificationService.createNotification(notification);
        ResponseMessage responseMessage = new ResponseMessage("Notification created successfully", (Object)savedNotification);
        return ResponseEntity.ok((Object)responseMessage);
    }

    @GetMapping(value={"/generate-pdf/{id}"})
    public void generateNotificationPdf(@PathVariable String id, HttpServletResponse response) throws IOException {
        Notification notification = this.notificationService.findNotificationById(id);
        this.notificationService.generateNotificationPdf(notification, response);
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteNotification(@PathVariable String id) {
        try {
            this.notificationService.deleteNotification(id);
            return ResponseEntity.ok((Object)new ResponseMessage("Notification deleted successfully", null));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Notification not found with ID: " + id, null));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("An error occurred while deleting the notification", null));
        }
    }

    @GetMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> getNotificationsBySchool(@PathVariable String schoolId) {
        List notifications = this.notificationService.getNotificationsBySchool(schoolId);
        return ResponseEntity.ok((Object)new ResponseMessage("Notifications fetched successfully for school", (Object)notifications));
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> createNotificationWithSchool(@RequestBody Notification notification, @PathVariable String schoolId) {
        Notification savedNotification = this.notificationService.createNotificationWithSchool(notification, schoolId);
        ResponseMessage responseMessage = new ResponseMessage("Notification created successfully for school", (Object)savedNotification);
        return ResponseEntity.ok((Object)responseMessage);
    }

    @GetMapping(value={"/generate-pdf/{id}/{schoolId}"})
    public void generateNotificationPdfWithSchool(@PathVariable String id, @PathVariable String schoolId, HttpServletResponse response) throws IOException {
        Notification notification = this.notificationService.findNotificationByIdAndSchool(id, schoolId);
        this.notificationService.generateNotificationPdf(notification, response);
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteNotificationWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        try {
            this.notificationService.deleteNotificationWithSchool(id, schoolId);
            return ResponseEntity.ok((Object)new ResponseMessage("Notification deleted successfully for school", null));
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Notification not found with ID: " + id + " for school: " + schoolId, null));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.INTERNAL_SERVER_ERROR).body((Object)new ResponseMessage("An error occurred while deleting the notification", null));
        }
    }
}

