/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.text.BaseColor
 *  com.itextpdf.text.Chunk
 *  com.itextpdf.text.Document
 *  com.itextpdf.text.DocumentException
 *  com.itextpdf.text.Element
 *  com.itextpdf.text.Font
 *  com.itextpdf.text.Font$FontFamily
 *  com.itextpdf.text.PageSize
 *  com.itextpdf.text.Paragraph
 *  com.itextpdf.text.Phrase
 *  com.itextpdf.text.Rectangle
 *  com.itextpdf.text.pdf.PdfContentByte
 *  com.itextpdf.text.pdf.PdfPCell
 *  com.itextpdf.text.pdf.PdfPTable
 *  com.itextpdf.text.pdf.PdfWriter
 *  com.itextpdf.text.pdf.draw.LineSeparator
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.School
 *  com.neel.StudentAdmission.model.TransferCertificate
 *  com.neel.StudentAdmission.model.TransferCertificateRequest
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  com.neel.StudentAdmission.repo.SchoolRepository
 *  com.neel.StudentAdmission.repo.TransferCertificateRepository
 *  com.neel.StudentAdmission.repo.TransferCertificateRequestRepository
 *  com.neel.StudentAdmission.service.EmailService
 *  com.neel.StudentAdmission.service.TransferCertificateService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.stereotype.Service
 *  org.springframework.web.multipart.MultipartFile
 */
package com.neel.StudentAdmission.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.School;
import com.neel.StudentAdmission.model.TransferCertificate;
import com.neel.StudentAdmission.model.TransferCertificateRequest;
import com.neel.StudentAdmission.repo.AdmissionRepository;
import com.neel.StudentAdmission.repo.SchoolRepository;
import com.neel.StudentAdmission.repo.TransferCertificateRepository;
import com.neel.StudentAdmission.repo.TransferCertificateRequestRepository;
import com.neel.StudentAdmission.service.EmailService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TransferCertificateService {
    @Autowired
    private TransferCertificateRepository transferCertificateRepository;
    @Autowired
    private TransferCertificateRequestRepository tcRequestRepository;
    @Autowired
    private AdmissionRepository admissionRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private EmailService emailService;
    @Value(value="${file.upload-dir}")
    private String FILE_UPLOAD_DIR;
    private static final String TC_OUTPUT_DIR = "id_cards/transfer_certificates/";
    private static final String TC_DOCS_DIR = "id_cards/transfer_certificates/documents/";
    private static final String TC_TEMPLATE_PATH = "src/main/resources/templates/tc_template.png";

    private String generateTCNumber(String schoolId, String academicYear) {
        String schoolSuffix = schoolId.substring(Math.max(0, schoolId.length() - 4));
        String year = academicYear.split("-")[0];
        long count = this.transferCertificateRepository.findBySchoolId(schoolId).stream().filter(tc -> tc.getAcademicYear().startsWith(year)).count() + 1L;
        return String.format("TC/%s/%s/%03d", schoolSuffix, year, count);
    }

    public TransferCertificateRequest requestTransferCertificate(String admissionNumber, String schoolId, String reasonForLeaving, List<MultipartFile> requiredDocs) throws IOException {
        if (this.tcRequestRepository.existsByAdmissionNumberAndSchoolIdAndStatusNot(admissionNumber, schoolId, "rejected")) {
            throw new RuntimeException("A transfer certificate request already exists for this student");
        }
        Admission student = (Admission)this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId).orElseThrow(() -> new RuntimeException("Student not found with admission number: " + admissionNumber));
        School school = (School)this.schoolRepository.findBySchoolId(schoolId).orElseThrow(() -> new RuntimeException("School not found with ID: " + schoolId));
        TransferCertificateRequest request = new TransferCertificateRequest();
        request.setAdmissionNumber(admissionNumber);
        request.setSchoolId(schoolId);
        request.setStudentName(student.getStudentName());
        request.setClassName(student.getClassName());
        request.setSection(student.getSection());
        request.setRequestDate(LocalDate.now());
        request.setReasonForLeaving(reasonForLeaving);
        request.setStatus("pending");
        ArrayList<String> docPaths = new ArrayList<String>();
        if (requiredDocs != null && !requiredDocs.isEmpty()) {
            File uploadDir = new File(TC_DOCS_DIR + admissionNumber + "/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            for (MultipartFile file : requiredDocs) {
                String originalFilename = file.getOriginalFilename();
                String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                Path filePath = Paths.get(uploadDir.getAbsolutePath(), uniqueFilename);
                Files.write(filePath, file.getBytes(), new OpenOption[0]);
                docPaths.add(filePath.toString());
            }
        }
        request.setRequiredDocumentPaths(docPaths);
        TransferCertificateRequest savedRequest = (TransferCertificateRequest)this.tcRequestRepository.save((Object)request);
        if (!student.isTcRequestNotificationSent()) {
            this.emailService.sendTCRequestNotification(savedRequest, student, school);
            student.setTcRequestNotificationSent(true);
            this.admissionRepository.save((Object)student);
        }
        return savedRequest;
    }

    public TransferCertificate approveTransferCertificateRequest(String requestId, String adminRemarks, TransferCertificate tcDetails) {
        TransferCertificateRequest request = (TransferCertificateRequest)this.tcRequestRepository.findById((Object)requestId).orElseThrow(() -> new RuntimeException("Transfer certificate request not found"));
        if (!"pending".equals(request.getStatus())) {
            throw new RuntimeException("This request has already been " + request.getStatus());
        }
        Admission student = (Admission)this.admissionRepository.findByAdmissionNumberAndSchoolId(request.getAdmissionNumber(), request.getSchoolId()).orElseThrow(() -> new RuntimeException("Student not found"));
        School school = (School)this.schoolRepository.findBySchoolId(request.getSchoolId()).orElseThrow(() -> new RuntimeException("School not found"));
        TransferCertificate tc = this.createTransferCertificate(request.getAdmissionNumber(), request.getSchoolId(), tcDetails);
        request.setStatus("approved");
        request.setAdminRemarks(adminRemarks);
        request.setApprovalDate(LocalDate.now());
        request.setTcId(tc.getId());
        this.tcRequestRepository.save((Object)request);
        if (!student.isTcApprovalNotificationSent()) {
            this.emailService.sendTCApprovalNotification(request, student, school);
            student.setTcApprovalNotificationSent(true);
            this.admissionRepository.save((Object)student);
        }
        return tc;
    }

    public TransferCertificateRequest rejectTransferCertificateRequest(String requestId, String adminRemarks) {
        TransferCertificateRequest request = (TransferCertificateRequest)this.tcRequestRepository.findById((Object)requestId).orElseThrow(() -> new RuntimeException("Transfer certificate request not found"));
        if (!"pending".equals(request.getStatus())) {
            throw new RuntimeException("This request has already been " + request.getStatus());
        }
        Admission student = (Admission)this.admissionRepository.findByAdmissionNumberAndSchoolId(request.getAdmissionNumber(), request.getSchoolId()).orElseThrow(() -> new RuntimeException("Student not found"));
        School school = (School)this.schoolRepository.findBySchoolId(request.getSchoolId()).orElseThrow(() -> new RuntimeException("School not found"));
        request.setStatus("rejected");
        request.setAdminRemarks(adminRemarks);
        request.setApprovalDate(LocalDate.now());
        TransferCertificateRequest savedRequest = (TransferCertificateRequest)this.tcRequestRepository.save((Object)request);
        this.emailService.sendTCRejectionNotification(savedRequest, student, school);
        return savedRequest;
    }

    public List<TransferCertificateRequest> getAllTransferCertificateRequests(String schoolId) {
        return this.tcRequestRepository.findBySchoolId(schoolId);
    }

    public List<TransferCertificateRequest> getPendingTransferCertificateRequests(String schoolId) {
        return this.tcRequestRepository.findBySchoolIdAndStatus(schoolId, "pending");
    }

    public List<TransferCertificateRequest> getTransferCertificateRequestStatus(String admissionNumber, String schoolId) {
        return this.tcRequestRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId);
    }

    public TransferCertificate createTransferCertificate(String admissionNumber, String schoolId, TransferCertificate tcDetails) {
        if (this.transferCertificateRepository.existsByAdmissionNumberAndSchoolId(admissionNumber, schoolId)) {
            throw new RuntimeException("Transfer certificate already exists for this student");
        }
        Admission student = (Admission)this.admissionRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId).orElseThrow(() -> new RuntimeException("Student not found with admission number: " + admissionNumber));
        School school = (School)this.schoolRepository.findBySchoolId(schoolId).orElseThrow(() -> new RuntimeException("School not found with ID: " + schoolId));
        TransferCertificate tc = new TransferCertificate();
        tc.setTcNumber(this.generateTCNumber(schoolId, student.getAcademicYear()));
        tc.setAdmissionNumber(admissionNumber);
        tc.setSchoolId(schoolId);
        tc.setStudentName(student.getStudentName());
        tc.setFatherName(student.getFatherName());
        tc.setMotherName(student.getMotherName());
        tc.setClassName(student.getClassName());
        tc.setSection(student.getSection());
        tc.setAcademicYear(student.getAcademicYear());
        tc.setIssueDate(LocalDate.now());
        if (tcDetails.getLastAttendanceDate() != null) {
            tc.setLastAttendanceDate(tcDetails.getLastAttendanceDate());
        } else {
            tc.setLastAttendanceDate(LocalDate.now());
        }
        tc.setReasonForLeaving(tcDetails.getReasonForLeaving());
        tc.setConductAndBehavior(tcDetails.getConductAndBehavior());
        tc.setRemarks(tcDetails.getRemarks());
        tc.setFeesCleared(tcDetails.isFeesCleared());
        String tcPath = this.generateTransferCertificate(tc, school);
        tc.setTcPath(tcPath);
        student.setStatus("transferred");
        this.admissionRepository.save((Object)student);
        return (TransferCertificate)this.transferCertificateRepository.save((Object)tc);
    }

    private String generateTransferCertificate(TransferCertificate tc, School school) {
        File outputDir = new File(TC_OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        String outputPath = "id_cards/transfer_certificates/tc_" + tc.getAdmissionNumber() + "_" + tc.getSchoolId() + ".pdf";
        return outputPath;
    }

    public TransferCertificate getTransferCertificateByNumber(String tcNumber) {
        return (TransferCertificate)this.transferCertificateRepository.findByTcNumber(tcNumber).orElseThrow(() -> new RuntimeException("Transfer certificate not found with number: " + tcNumber));
    }

    public TransferCertificate getTransferCertificateById(String id, String schoolId) {
        return (TransferCertificate)this.transferCertificateRepository.findByIdAndSchoolId(id, schoolId).orElseThrow(() -> new RuntimeException("Transfer certificate not found with number: " + id));
    }

    public TransferCertificate getTransferCertificateByAdmission(String admissionNumber, String schoolId) {
        return (TransferCertificate)this.transferCertificateRepository.findByAdmissionNumberAndSchoolId(admissionNumber, schoolId).orElseThrow(() -> new RuntimeException("Transfer certificate not found for student: " + admissionNumber));
    }

    public List<TransferCertificate> getAllTransferCertificates(String schoolId) {
        return this.transferCertificateRepository.findBySchoolId(schoolId);
    }

    public File generateTransferCertificatePDF(TransferCertificate tc) {
        try {
            String baseDir = System.getProperty("user.dir");
            File outputDir = new File(baseDir, TC_OUTPUT_DIR);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            String fileName = "tc_" + tc.getAdmissionNumber() + "_" + tc.getSchoolId() + ".pdf";
            File outputFile = new File(outputDir, fileName);
            System.out.println("Generating PDF at: " + outputFile.getAbsolutePath());
            Admission student = (Admission)this.admissionRepository.findByAdmissionNumberAndSchoolId(tc.getAdmissionNumber(), tc.getSchoolId()).orElseThrow(() -> new RuntimeException("Student not found"));
            School school = (School)this.schoolRepository.findBySchoolId(tc.getSchoolId()).orElseThrow(() -> new RuntimeException("School not found"));
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance((Document)document, (OutputStream)new FileOutputStream(outputFile));
            document.open();
            document.setMargins(36.0f, 36.0f, 36.0f, 36.0f);
            BaseColor primaryColor = new BaseColor(0, 62, 126);
            BaseColor accentColor = new BaseColor(220, 53, 69);
            BaseColor secondaryColor = new BaseColor(40, 167, 69);
            BaseColor lightBgColor = new BaseColor(240, 248, 255);
            BaseColor goldAccent = new BaseColor(218, 165, 32);
            BaseColor headerBgColor = new BaseColor(248, 249, 250);
            Font schoolNameFont = new Font(Font.FontFamily.HELVETICA, 22.0f, 1, primaryColor);
            Font schoolAddressFont = new Font(Font.FontFamily.HELVETICA, 10.0f);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20.0f, 1, BaseColor.WHITE);
            Font sectionTitleFont = new Font(Font.FontFamily.HELVETICA, 14.0f, 1, accentColor);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 11.0f, 1, primaryColor);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 11.0f);
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 10.0f, 2);
            PdfContentByte canvas = writer.getDirectContent();
            for (int i = 0; i < 3; ++i) {
                Rectangle outerRect = new Rectangle(document.left() - 10.0f + (float)i, document.bottom() - 10.0f + (float)i, document.right() + 10.0f - (float)i, document.top() + 10.0f - (float)i);
                outerRect.setBorder(15);
                outerRect.setBorderWidth(1.0f);
                if (i % 2 == 0) {
                    outerRect.setBorderColor(goldAccent);
                } else {
                    outerRect.setBorderColor(primaryColor);
                }
                canvas.rectangle(outerRect);
            }
            Rectangle innerRect = new Rectangle(document.left() - 5.0f, document.bottom() - 5.0f, document.right() + 5.0f, document.top() + 5.0f);
            innerRect.setBorder(15);
            innerRect.setBorderWidth(1.5f);
            innerRect.setBorderColor(accentColor);
            canvas.rectangle(innerRect);
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100.0f);
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBorder(15);
            headerCell.setBorderColor(goldAccent);
            headerCell.setBorderWidth(2.0f);
            headerCell.setPadding(15.0f);
            headerCell.setBackgroundColor(headerBgColor);
            Paragraph schoolNamePara = new Paragraph(school.getSchoolName(), schoolNameFont);
            schoolNamePara.setAlignment(1);
            headerCell.addElement((Element)schoolNamePara);
            LineSeparator nameDivider = new LineSeparator();
            nameDivider.setLineWidth(1.5f);
            nameDivider.setPercentage(70.0f);
            nameDivider.setLineColor(goldAccent);
            nameDivider.setAlignment(1);
            Paragraph dividerPara = new Paragraph();
            dividerPara.add((Element)nameDivider);
            dividerPara.setSpacingBefore(5.0f);
            dividerPara.setSpacingAfter(5.0f);
            headerCell.addElement((Element)dividerPara);
            String addressText = school.getSchoolAddress() != null ? school.getSchoolAddress() : "School Address, City, State - PIN";
            Paragraph schoolAddressPara = new Paragraph(addressText, schoolAddressFont);
            schoolAddressPara.setAlignment(1);
            headerCell.addElement((Element)schoolAddressPara);
            String contactText = "Phone: " + (school.getSchoolPhone() != null ? school.getSchoolPhone() : "(123) 456-7890") + " | Email: " + (school.getSchoolEmail() != null ? school.getSchoolEmail() : "info@school.edu");
            Paragraph schoolContactPara = new Paragraph(contactText, schoolAddressFont);
            schoolContactPara.setAlignment(1);
            headerCell.addElement((Element)schoolContactPara);
            headerTable.addCell(headerCell);
            document.add((Element)headerTable);
            document.add((Element)Chunk.NEWLINE);
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100.0f);
            PdfPCell titleCell = new PdfPCell();
            titleCell.setBorder(15);
            titleCell.setBorderColor(goldAccent);
            titleCell.setBorderWidth(1.5f);
            titleCell.setPadding(12.0f);
            titleCell.setBackgroundColor(primaryColor);
            Paragraph titlePara = new Paragraph("TRANSFER CERTIFICATE", titleFont);
            titlePara.setAlignment(1);
            titleCell.addElement((Element)titlePara);
            titleTable.addCell(titleCell);
            document.add((Element)titleTable);
            PdfPTable decorTable = new PdfPTable(5);
            decorTable.setWidthPercentage(100.0f);
            BaseColor[] patternColors = new BaseColor[]{goldAccent, accentColor, secondaryColor, accentColor, goldAccent};
            for (int i = 0; i < 5; ++i) {
                PdfPCell decorCell = new PdfPCell();
                decorCell.setBorder(0);
                decorCell.setBackgroundColor(patternColors[i]);
                decorCell.setFixedHeight(4.0f);
                decorTable.addCell(decorCell);
            }
            document.add((Element)decorTable);
            document.add((Element)Chunk.NEWLINE);
            PdfPTable topTable = new PdfPTable(2);
            topTable.setWidthPercentage(100.0f);
            topTable.setSpacingBefore(10.0f);
            PdfPCell certNoLabelCell = new PdfPCell(new Phrase("Certificate No.:", labelFont));
            certNoLabelCell.setBorder(0);
            certNoLabelCell.setPaddingBottom(10.0f);
            certNoLabelCell.setBackgroundColor(lightBgColor);
            topTable.addCell(certNoLabelCell);
            PdfPCell certNoValueCell = new PdfPCell(new Phrase(tc.getTcNumber(), valueFont));
            certNoValueCell.setBorder(2);
            certNoValueCell.setBorderColor(accentColor);
            certNoValueCell.setBorderWidth(1.5f);
            certNoValueCell.setPaddingBottom(10.0f);
            certNoValueCell.setBackgroundColor(lightBgColor);
            topTable.addCell(certNoValueCell);
            PdfPCell issueDateLabelCell = new PdfPCell(new Phrase("Issue Date:", labelFont));
            issueDateLabelCell.setBorder(0);
            issueDateLabelCell.setPaddingBottom(10.0f);
            issueDateLabelCell.setBackgroundColor(lightBgColor);
            topTable.addCell(issueDateLabelCell);
            String issueDate = tc.getIssueDate() != null ? tc.getIssueDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
            PdfPCell issueDateValueCell = new PdfPCell(new Phrase(issueDate, valueFont));
            issueDateValueCell.setBorder(2);
            issueDateValueCell.setBorderColor(accentColor);
            issueDateValueCell.setBorderWidth(1.5f);
            issueDateValueCell.setPaddingBottom(10.0f);
            issueDateValueCell.setBackgroundColor(lightBgColor);
            topTable.addCell(issueDateValueCell);
            document.add((Element)topTable);
            document.add((Element)Chunk.NEWLINE);
            this.addFancySectionHeader(document, "Student Details", sectionTitleFont, accentColor, goldAccent);
            PdfPTable studentTable = new PdfPTable(2);
            studentTable.setWidthPercentage(100.0f);
            studentTable.setSpacingBefore(10.0f);
            this.addFancyTableRow(studentTable, "Name:", student.getStudentName(), labelFont, valueFont, accentColor, lightBgColor, false);
            this.addFancyTableRow(studentTable, "Admission No.:", tc.getAdmissionNumber(), labelFont, valueFont, secondaryColor, lightBgColor, true);
            String dobFormatted = student.getDob() != null && !student.getDob().isEmpty() ? this.formatDateWithSlashes(student.getDob()) : "";
            this.addFancyTableRow(studentTable, "Date of Birth:", dobFormatted, labelFont, valueFont, accentColor, lightBgColor, false);
            this.addFancyTableRow(studentTable, "Class Last Attended:", tc.getClassName() + " " + tc.getSection(), labelFont, valueFont, secondaryColor, lightBgColor, true);
            document.add((Element)studentTable);
            document.add((Element)Chunk.NEWLINE);
            this.addFancySectionHeader(document, "Enrollment Information", sectionTitleFont, secondaryColor, goldAccent);
            PdfPTable enrollmentTable = new PdfPTable(2);
            enrollmentTable.setWidthPercentage(100.0f);
            enrollmentTable.setSpacingBefore(10.0f);
            this.addFancyTableRow(enrollmentTable, "Parent/Guardian:", student.getFatherName(), labelFont, valueFont, accentColor, lightBgColor, false);
            String admissionDate = student.getAdmissionDate() != null && !student.getAdmissionDate().isEmpty() ? this.formatDateWithSlashes(student.getAdmissionDate()) : "";
            this.addFancyTableRow(enrollmentTable, "Date of Admission:", admissionDate, labelFont, valueFont, secondaryColor, lightBgColor, true);
            String leavingDate = tc.getLastAttendanceDate() != null ? tc.getLastAttendanceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
            this.addFancyTableRow(enrollmentTable, "Date of Leaving:", leavingDate, labelFont, valueFont, accentColor, lightBgColor, false);
            this.addFancyTableRow(enrollmentTable, "Reason for Transfer:", tc.getReasonForLeaving(), labelFont, valueFont, secondaryColor, lightBgColor, true);
            document.add((Element)enrollmentTable);
            document.add((Element)Chunk.NEWLINE);
            this.addFancySectionHeader(document, "Conduct & Remarks", sectionTitleFont, accentColor, goldAccent);
            PdfPTable conductTable = new PdfPTable(1);
            conductTable.setWidthPercentage(100.0f);
            conductTable.setSpacingBefore(10.0f);
            PdfPCell conductLabelCell = new PdfPCell(new Phrase("Conduct:", labelFont));
            conductLabelCell.setBorder(0);
            conductLabelCell.setPaddingBottom(5.0f);
            conductLabelCell.setBackgroundColor(lightBgColor);
            conductTable.addCell(conductLabelCell);
            PdfPCell conductValueCell = new PdfPCell(new Phrase(tc.getConductAndBehavior() != null ? tc.getConductAndBehavior() : "", valueFont));
            conductValueCell.setBorder(2);
            conductValueCell.setBorderColor(accentColor);
            conductValueCell.setBorderWidth(1.5f);
            conductValueCell.setPaddingBottom(10.0f);
            conductValueCell.setBackgroundColor(lightBgColor);
            conductTable.addCell(conductValueCell);
            document.add((Element)conductTable);
            PdfPTable feesTable = new PdfPTable(1);
            feesTable.setWidthPercentage(100.0f);
            feesTable.setSpacingBefore(10.0f);
            PdfPCell feesLabelCell = new PdfPCell(new Phrase("Whether the student has paid all dues (Yes/No):", labelFont));
            feesLabelCell.setBorder(0);
            feesLabelCell.setPaddingBottom(5.0f);
            feesLabelCell.setBackgroundColor(lightBgColor);
            feesTable.addCell(feesLabelCell);
            PdfPCell feesValueCell = new PdfPCell(new Phrase(tc.isFeesCleared() ? "Yes" : "No", valueFont));
            feesValueCell.setBorder(2);
            feesValueCell.setBorderColor(secondaryColor);
            feesValueCell.setBorderWidth(1.5f);
            feesValueCell.setPaddingBottom(10.0f);
            feesValueCell.setBackgroundColor(lightBgColor);
            feesTable.addCell(feesValueCell);
            document.add((Element)feesTable);
            document.add((Element)Chunk.NEWLINE);
            document.add((Element)Chunk.NEWLINE);
            PdfPTable decorEndTable = new PdfPTable(5);
            decorEndTable.setWidthPercentage(100.0f);
            for (int i = 4; i >= 0; --i) {
                PdfPCell decorEndCell = new PdfPCell();
                decorEndCell.setBorder(0);
                decorEndCell.setBackgroundColor(patternColors[i]);
                decorEndCell.setFixedHeight(4.0f);
                decorEndTable.addCell(decorEndCell);
            }
            document.add((Element)decorEndTable);
            PdfPTable signatureTable = new PdfPTable(3);
            signatureTable.setWidthPercentage(100.0f);
            signatureTable.setSpacingBefore(40.0f);
            PdfPCell cell1 = new PdfPCell(new Phrase("Class Teacher", footerFont));
            cell1.setBorder(1);
            cell1.setBorderColor(accentColor);
            cell1.setBorderWidth(1.5f);
            cell1.setHorizontalAlignment(1);
            cell1.setPaddingTop(40.0f);
            PdfPCell cell2 = new PdfPCell(new Phrase("Principal's Signature", footerFont));
            cell2.setBorder(1);
            cell2.setBorderColor(primaryColor);
            cell2.setBorderWidth(1.5f);
            cell2.setHorizontalAlignment(1);
            cell2.setPaddingTop(40.0f);
            PdfPCell cell3 = new PdfPCell(new Phrase("School Seal", footerFont));
            cell3.setBorder(1);
            cell3.setBorderColor(secondaryColor);
            cell3.setBorderWidth(1.5f);
            cell3.setHorizontalAlignment(1);
            cell3.setPaddingTop(40.0f);
            signatureTable.addCell(cell1);
            signatureTable.addCell(cell2);
            signatureTable.addCell(cell3);
            document.add((Element)signatureTable);
            document.close();
            tc.setTcPath(TC_OUTPUT_DIR + fileName);
            this.transferCertificateRepository.save((Object)tc);
            System.out.println("PDF generation completed. File exists: " + outputFile.exists());
            return outputFile;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addFancySectionHeader(Document document, String title, Font font, BaseColor color, BaseColor accentColor) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100.0f);
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(2);
        titleCell.setBorderColor(accentColor);
        titleCell.setBorderWidth(2.0f);
        titleCell.setPaddingBottom(8.0f);
        titleCell.setPaddingLeft(5.0f);
        Paragraph titlePara = new Paragraph(title, font);
        titleCell.addElement((Element)titlePara);
        headerTable.addCell(titleCell);
        document.add((Element)headerTable);
    }

    private void addFormField(Document document, String label, String value, Font labelFont, Font valueFont) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.add((Element)new Chunk(label + " ", labelFont));
        Chunk valueChunk = new Chunk(value, valueFont);
        paragraph.add((Element)valueChunk);
        document.add((Element)paragraph);
        LineSeparator line = new LineSeparator();
        line.setLineWidth(0.5f);
        line.setPercentage(100.0f);
        document.add((Element)line);
    }

    private void addTableCell(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        Paragraph paragraph = new Paragraph();
        paragraph.add((Element)new Chunk(label + " ", labelFont));
        String safeValue = value != null ? value : "";
        paragraph.add((Element)new Chunk(safeValue, valueFont));
        cell.addElement((Element)paragraph);
        table.addCell(cell);
    }

    private String formatDateWithSlashes(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "";
        }
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        catch (Exception e) {
            return dateStr;
        }
    }

    private void addSectionHeader(Document document, String title, Font font, BaseColor color) throws DocumentException {
        Paragraph sectionTitle = new Paragraph(title, font);
        document.add((Element)sectionTitle);
        LineSeparator line = new LineSeparator();
        line.setLineColor(color);
        line.setLineWidth(1.0f);
        line.setPercentage(100.0f);
        document.add((Element)line);
    }

    private void addFancyTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont, BaseColor borderColor, BaseColor bgColor, boolean alternate) {
        BaseColor rowColor = alternate ? bgColor : BaseColor.WHITE;
        PdfPCell labelCell = new PdfPCell();
        labelCell.setBorder(0);
        labelCell.setPaddingBottom(8.0f);
        labelCell.setPaddingTop(5.0f);
        labelCell.setPaddingLeft(5.0f);
        labelCell.setBackgroundColor(rowColor);
        Paragraph labelPara = new Paragraph(label, labelFont);
        labelCell.addElement((Element)labelPara);
        table.addCell(labelCell);
        String safeValue = value != null ? value : "";
        PdfPCell valueCell = new PdfPCell();
        valueCell.setBorder(2);
        valueCell.setBorderColor(borderColor);
        valueCell.setBorderWidth(1.5f);
        valueCell.setPaddingBottom(8.0f);
        valueCell.setPaddingTop(5.0f);
        valueCell.setBackgroundColor(rowColor);
        Paragraph valuePara = new Paragraph(safeValue, valueFont);
        valueCell.addElement((Element)valuePara);
        table.addCell(valueCell);
    }

    private void addStyledTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont, BaseColor borderColor) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(0);
        labelCell.setPaddingBottom(5.0f);
        table.addCell(labelCell);
        String safeValue = value != null ? value : "";
        PdfPCell valueCell = new PdfPCell(new Phrase(safeValue, valueFont));
        valueCell.setBorder(2);
        valueCell.setBorderColor(borderColor);
        valueCell.setPaddingBottom(10.0f);
        table.addCell(valueCell);
    }

    private void addEnhancedSectionHeader(Document document, String title, Font font, BaseColor color, BaseColor accentColor) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100.0f);
        PdfPCell titleCell = new PdfPCell(new Phrase(title, font));
        titleCell.setBorder(2);
        titleCell.setBorderColor(accentColor);
        titleCell.setBorderWidth(1.0f);
        titleCell.setPaddingBottom(5.0f);
        titleCell.setPaddingLeft(0.0f);
        headerTable.addCell(titleCell);
        document.add((Element)headerTable);
    }

    private void addStyledTableRowAlternating(PdfPTable table, String label, String value, Font labelFont, Font valueFont, BaseColor borderColor, BaseColor bgColor, boolean alternate) {
        BaseColor rowColor = alternate ? bgColor : BaseColor.WHITE;
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(0);
        labelCell.setPaddingBottom(5.0f);
        labelCell.setBackgroundColor(rowColor);
        table.addCell(labelCell);
        String safeValue = value != null ? value : "";
        PdfPCell valueCell = new PdfPCell(new Phrase(safeValue, valueFont));
        valueCell.setBorder(2);
        valueCell.setBorderColor(borderColor);
        valueCell.setPaddingBottom(10.0f);
        valueCell.setBackgroundColor(rowColor);
        table.addCell(valueCell);
    }
}

