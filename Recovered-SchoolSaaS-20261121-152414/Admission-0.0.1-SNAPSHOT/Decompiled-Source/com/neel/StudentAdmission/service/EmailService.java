/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.model.TransferCertificateRequest
 *  com.neel.StudentAdmission.service.EmailService
 *  jakarta.mail.MessagingException
 *  jakarta.mail.internet.MimeMessage
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.mail.javamail.JavaMailSender
 *  org.springframework.mail.javamail.MimeMessageHelper
 *  org.springframework.stereotype.Service
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.model.TransferCertificateRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value(value="${spring.mail.username}")
    private String fromEmail;

    public void sendTCRequestNotification(TransferCertificateRequest request, Admission student, School school) {
        try {
            if (student.getEmail() != null && !student.getEmail().isEmpty()) {
                this.sendTCRequestEmailToStudent(request, student, school);
            }
            if (school.getSchoolEmail() != null && !school.getSchoolEmail().isEmpty()) {
                this.sendTCRequestEmailToAdmin(request, student, school);
            }
        }
        catch (Exception e) {
            System.err.println("Failed to send TC request notification email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendTCApprovalNotification(TransferCertificateRequest request, Admission student, School school) {
        try {
            if (student.getEmail() != null && !student.getEmail().isEmpty()) {
                this.sendTCApprovalEmailToStudent(request, student, school);
            }
            if (school.getSchoolEmail() != null && !school.getSchoolEmail().isEmpty()) {
                this.sendTCApprovalEmailToAdmin(request, student, school);
            }
        }
        catch (Exception e) {
            System.err.println("Failed to send TC approval notification email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendTCRejectionNotification(TransferCertificateRequest request, Admission student, School school) {
        try {
            if (student.getEmail() != null && !student.getEmail().isEmpty()) {
                this.sendTCRejectionEmailToStudent(request, student, school);
            }
            if (school.getSchoolEmail() != null && !school.getSchoolEmail().isEmpty()) {
                this.sendTCRejectionEmailToAdmin(request, student, school);
            }
        }
        catch (Exception e) {
            System.err.println("Failed to send TC rejection notification email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendTCRequestEmailToStudent(TransferCertificateRequest request, Admission student, School school) throws MessagingException {
        String subject = "Transfer Certificate Request Submitted - " + school.getSchoolName();
        String body = this.getEmailTemplate("Transfer Certificate Request Confirmation", "#4285F4", school.getSchoolName(), student.getStudentName(), "<p>Your request for a Transfer Certificate has been successfully submitted.</p><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #4285F4; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #4285F4;'>Request Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Request Date:</strong></td><td>" + request.getRequestDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Admission Number:</strong></td><td>" + student.getAdmissionNumber() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Class:</strong></td><td>" + student.getClassName() + " " + student.getSection() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Reason for Leaving:</strong></td><td>" + request.getReasonForLeaving() + "</td></tr>  </table></div><p>Your request is currently under review by the school administration. You will be notified once it has been processed.</p><div style='margin-top: 30px; padding: 10px 15px; background-color: #f0f7ff; border-radius: 4px;'>  <p style='margin: 0; font-size: 14px;'><strong>Note:</strong> Please contact the school office if you have any questions regarding your request.</p></div>");
        this.sendEmail(student.getEmail(), subject, body);
    }

    private void sendTCRequestEmailToAdmin(TransferCertificateRequest request, Admission student, School school) throws MessagingException {
        String subject = "New Transfer Certificate Request - " + student.getStudentName();
        String body = this.getEmailTemplate("New Transfer Certificate Request", "#EA4335", school.getSchoolName(), "Admin", "<p>A new Transfer Certificate request has been submitted.</p><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #EA4335; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #EA4335;'>Student Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Name:</strong></td><td>" + student.getStudentName() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Admission Number:</strong></td><td>" + student.getAdmissionNumber() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Class:</strong></td><td>" + student.getClassName() + " " + student.getSection() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Parent's Name:</strong></td><td>" + student.getFatherName() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Contact:</strong></td><td>" + student.getPhone() + "</td></tr>  </table></div><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #EA4335; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #EA4335;'>Request Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Request Date:</strong></td><td>" + request.getRequestDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Reason for Leaving:</strong></td><td>" + request.getReasonForLeaving() + "</td></tr>  </table></div><div style='margin-top: 25px;'>  <a href='#' style='display: inline-block; padding: 10px 20px; background-color: #EA4335; color: white; text-decoration: none; border-radius: 4px; font-weight: bold;'>Review Request</a></div>");
        this.sendEmail(school.getSchoolEmail(), subject, body);
    }

    private void sendTCApprovalEmailToStudent(TransferCertificateRequest request, Admission student, School school) throws MessagingException {
        String subject = "Transfer Certificate Request Approved - " + school.getSchoolName();
        String body = this.getEmailTemplate("Transfer Certificate Request Approved", "#34A853", school.getSchoolName(), student.getStudentName(), "<div style='text-align: center; margin-bottom: 30px;'>  <div style='display: inline-block; width: 80px; height: 80px; background-color: #34A853; border-radius: 50%; margin-bottom: 15px;'>    <div style='font-size: 50px; color: white; line-height: 80px;'>\u2713</div>  </div>  <h2 style='color: #34A853; margin: 0;'>Approved</h2></div><p>Your request for a Transfer Certificate has been <strong>approved</strong>.</p><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #34A853; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #34A853;'>Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Approval Date:</strong></td><td>" + request.getApprovalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Admission Number:</strong></td><td>" + student.getAdmissionNumber() + "</td></tr>  </table></div><p>You can download your Transfer Certificate from the school portal or collect it from the school office.</p><div style='margin-top: 25px;'>  <a href='#' style='display: inline-block; padding: 10px 20px; background-color: #34A853; color: white; text-decoration: none; border-radius: 4px; font-weight: bold;'>Download Certificate</a></div><div style='margin-top: 30px; padding: 15px; background-color: #f0f7ff; border-radius: 4px;'>  <p style='margin: 0; font-size: 14px;'><strong>Note:</strong> We wish you all the best for your future endeavors.</p></div>");
        this.sendEmail(student.getEmail(), subject, body);
    }

    private void sendTCApprovalEmailToAdmin(TransferCertificateRequest request, Admission student, School school) throws MessagingException {
        String subject = "Transfer Certificate Approved - " + student.getStudentName();
        String body = this.getEmailTemplate("Transfer Certificate Approved", "#34A853", school.getSchoolName(), "Admin", "<p>The Transfer Certificate request for the following student has been approved:</p><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #34A853; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #34A853;'>Student Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Name:</strong></td><td>" + student.getStudentName() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Admission Number:</strong></td><td>" + student.getAdmissionNumber() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Class:</strong></td><td>" + student.getClassName() + " " + student.getSection() + "</td></tr>  </table></div><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #34A853; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #34A853;'>Approval Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Approval Date:</strong></td><td>" + request.getApprovalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Remarks:</strong></td><td>" + (request.getAdminRemarks() != null ? request.getAdminRemarks() : "-") + "</td></tr>  </table></div><div style='margin-top: 20px; padding: 10px 15px; background-color: #e8f5e9; border-radius: 4px;'>  <p style='margin: 0; font-size: 14px;'><strong>Note:</strong> The student's status has been updated to 'transferred' in the system.</p></div><div style='margin-top: 25px;'>  <a href='#' style='display: inline-block; padding: 10px 20px; background-color: #34A853; color: white; text-decoration: none; border-radius: 4px; font-weight: bold;'>View Certificate</a></div>");
        this.sendEmail(school.getSchoolEmail(), subject, body);
    }

    private void sendTCRejectionEmailToStudent(TransferCertificateRequest request, Admission student, School school) throws MessagingException {
        String subject = "Transfer Certificate Request Status - " + school.getSchoolName();
        String body = this.getEmailTemplate("Transfer Certificate Request Update", "#FBBC05", school.getSchoolName(), student.getStudentName(), "<div style='text-align: center; margin-bottom: 30px;'>  <div style='display: inline-block; width: 80px; height: 80px; background-color: #FBBC05; border-radius: 50%; margin-bottom: 15px;'>    <div style='font-size: 50px; color: white; line-height: 80px;'>!</div>  </div></div><p>We regret to inform you that your request for a Transfer Certificate could not be processed at this time.</p><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #FBBC05; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #FBBC05;'>Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Decision Date:</strong></td><td>" + request.getApprovalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Remarks:</strong></td><td>" + (request.getAdminRemarks() != null ? request.getAdminRemarks() : "-") + "</td></tr>  </table></div><div style='margin-top: 30px; padding: 15px; background-color: #fff8e1; border-radius: 4px;'>  <p style='margin: 0; font-size: 14px;'><strong>Note:</strong> Please contact the school administration for more information.</p></div><div style='margin-top: 25px;'>  <a href='tel:" + school.getSchoolPhone() + "' style='display: inline-block; padding: 10px 20px; background-color: #FBBC05; color: white; text-decoration: none; border-radius: 4px; font-weight: bold;'>Contact School</a></div>");
        this.sendEmail(student.getEmail(), subject, body);
    }

    private void sendTCRejectionEmailToAdmin(TransferCertificateRequest request, Admission student, School school) throws MessagingException {
        String subject = "Transfer Certificate Request Rejected - " + student.getStudentName();
        String body = this.getEmailTemplate("Transfer Certificate Request Rejected", "#FBBC05", school.getSchoolName(), "Admin", "<p>The Transfer Certificate request for the following student has been rejected:</p><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #FBBC05; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #FBBC05;'>Student Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Name:</strong></td><td>" + student.getStudentName() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Admission Number:</strong></td><td>" + student.getAdmissionNumber() + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Class:</strong></td><td>" + student.getClassName() + " " + student.getSection() + "</td></tr>  </table></div><div style='margin: 20px 0; padding: 15px; background-color: #f8f9fa; border-left: 4px solid #FBBC05; border-radius: 4px;'>  <h3 style='margin-top: 0; color: #FBBC05;'>Rejection Details</h3>  <table style='width: 100%; border-collapse: collapse;'>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Rejection Date:</strong></td><td>" + request.getApprovalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</td></tr>    <tr><td style='padding: 8px 0; width: 40%; color: #555;'><strong>Remarks:</strong></td><td>" + (request.getAdminRemarks() != null ? request.getAdminRemarks() : "-") + "</td></tr>  </table></div><div style='margin-top: 25px;'>  <a href='#' style='display: inline-block; padding: 10px 20px; background-color: #FBBC05; color: white; text-decoration: none; border-radius: 4px; font-weight: bold;'>View Request</a></div>");
        this.sendEmail(school.getSchoolEmail(), subject, body);
    }

    private String getEmailTemplate(String title, String accentColor, String schoolName, String recipientName, String content) {
        return "<!DOCTYPE html><html><head>  <meta charset='UTF-8'>  <meta name='viewport' content='width=device-width, initial-scale=1.0'>  <title>" + title + "</title></head><body style='margin: 0; padding: 0; font-family: Arial, sans-serif; color: #333; background-color: #f5f5f5;'>  <div style='max-width: 600px; margin: 0 auto; background-color: #ffffff;'>    <!-- Header -->    <div style='background-color: " + accentColor + "; padding: 20px; text-align: center;'>      <h1 style='color: white; margin: 0; font-size: 24px;'>" + title + "</h1>    </div>    <!-- School Logo/Name -->    <div style='text-align: center; padding: 20px; border-bottom: 1px solid #eee;'>      <h2 style='margin: 0; color: #555;'>" + schoolName + "</h2>    </div>    <!-- Content -->    <div style='padding: 30px 20px;'>      <p style='margin-top: 0;'>Dear <strong>" + recipientName + "</strong>,</p>      " + content + "    </div>    <!-- Footer -->    <div style='background-color: #f5f5f5; padding: 20px; text-align: center; font-size: 12px; color: #777;'>      <p style='margin: 0;'>This is an automated message. Please do not reply to this email.</p>      <p style='margin: 10px 0 0;'>&copy; " + Year.now().getValue() + " " + schoolName + ". All rights reserved.</p>    </div>  </div></body></html>";
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(this.fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        this.mailSender.send(message);
    }
}

