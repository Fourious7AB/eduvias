/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.itextpdf.io.image.ImageDataFactory
 *  com.itextpdf.kernel.colors.Color
 *  com.itextpdf.kernel.colors.ColorConstants
 *  com.itextpdf.kernel.colors.DeviceRgb
 *  com.itextpdf.kernel.font.PdfFont
 *  com.itextpdf.kernel.font.PdfFontFactory
 *  com.itextpdf.kernel.pdf.PdfDocument
 *  com.itextpdf.kernel.pdf.PdfWriter
 *  com.itextpdf.layout.Document
 *  com.itextpdf.layout.borders.Border
 *  com.itextpdf.layout.borders.SolidBorder
 *  com.itextpdf.layout.element.Cell
 *  com.itextpdf.layout.element.IBlockElement
 *  com.itextpdf.layout.element.Image
 *  com.itextpdf.layout.element.Paragraph
 *  com.itextpdf.layout.element.Table
 *  com.itextpdf.layout.properties.HorizontalAlignment
 *  com.itextpdf.layout.properties.TextAlignment
 *  com.neel.RoutineMain.model.ExamRoutine
 *  com.neel.RoutineMain.model.ExamRoutine$SubjectExam
 *  com.neel.RoutineMain.model.ResponseMessage
 *  com.neel.RoutineMain.model.School
 *  com.neel.RoutineMain.repo.ExamRoutineRepository
 *  com.neel.RoutineMain.service.ExamRoutineService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestTemplate
 */
package com.neel.RoutineMain.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.neel.RoutineMain.model.ExamRoutine;
import com.neel.RoutineMain.model.ResponseMessage;
import com.neel.RoutineMain.model.School;
import com.neel.RoutineMain.repo.ExamRoutineRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExamRoutineService {
    @Autowired
    private ExamRoutineRepository repository;
    @Autowired
    private RestTemplate restTemplate;
    @Value(value="${School.Service.details.url}")
    private String schoolDetailsUrl;
    private PdfFont bold;
    private PdfFont italic;

    @jakarta.annotation.PostConstruct
    public void init() {
        try {
            this.bold = PdfFontFactory.createFont("Helvetica-Bold");
            this.italic = PdfFontFactory.createFont("Helvetica-Oblique");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExamRoutine createRoutine(ExamRoutine routine) {
        boolean routineExists = this.repository.existsByClassNameAndExamTypeAndSchoolId(routine.getClassName(), routine.getExamType(), routine.getSchoolId());
        if (routineExists) {
            throw new RuntimeException("Exam routine for this class already exists.");
        }
        return (ExamRoutine)this.repository.save(routine);
    }

    public ExamRoutine updateRoutine(String id, ExamRoutine updatedRoutine) {
        updatedRoutine.setSchoolId("67ca9167d56ec91fc9539234");
        Optional existingRoutine = this.repository.findById(id);
        if (!existingRoutine.isPresent()) {
            throw new RuntimeException("Routine not found.");
        }
        updatedRoutine.setId(id);
        return (ExamRoutine)this.repository.save(updatedRoutine);
    }

    public List<ExamRoutine> getAllRoutines() {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.repository.findBySchoolId(schoolId);
    }

    public List<ExamRoutine> getRoutineByClass(String className, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.repository.findByClassNameAndExamTypeAndSchoolId(className, examType, schoolId);
    }

    public void deleteRoutine(String id) {
        if (!this.repository.existsById(id)) {
            throw new RuntimeException("Routine not found.");
        }
        this.repository.deleteById(id);
    }

    public byte[] generateRoutinePdf(String className, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        List routines = this.repository.findByClassNameAndExamTypeAndSchoolId(className, examType, schoolId);
        if (routines.isEmpty()) {
            throw new RuntimeException("No routines found for Class: " + className + " this ExamType " + examType);
        }
        return this.generatePdf(routines, className, examType);
    }

    private School getSchoolData(String schoolId) {
        try {
            String url = this.schoolDetailsUrl + schoolId;
            ResponseEntity response = this.restTemplate.getForEntity(url, ResponseMessage.class, new Object[0]);
            System.out.println(" i m from response : " + response);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                System.out.println(" after successfull : " + ((ResponseMessage)response.getBody()).getData());
                return (School)((ResponseMessage)response.getBody()).getData();
            }
            return null;
        }
        catch (Exception e) {
            System.err.println("Error fetching school data: " + e.getMessage());
            return null;
        }
    }

    private byte[] generatePdf(List<ExamRoutine> routines, String className, String examType) {
        byte[] object;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter((OutputStream)out);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);
            document.setMargins(36.0f, 36.0f, 36.0f, 36.0f);
            String schoolId = routines.get(0).getSchoolId();
            System.out.println("Generating PDF for school ID: " + schoolId);
            School school = this.getSchoolData(schoolId);
            System.out.println(" from school data : " + school);
            Table headerTable = new Table(1).useAllAvailableWidth();
            if (school != null && school.getSchoolLogo() != null && !school.getSchoolLogo().isEmpty()) {
                try {
                    System.out.println("Adding school logo from: " + school.getSchoolLogo());
                    Image schoolLogo = new Image(ImageDataFactory.create((URL)new URL(school.getSchoolLogo())));
                    schoolLogo.setWidth(100.0f);
                    schoolLogo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    Cell logoCell = ((Cell)new Cell().setBorder(null)).add(schoolLogo);
                    logoCell.setTextAlignment(TextAlignment.CENTER);
                    headerTable.addCell(logoCell);
                    Cell schoolNameCell = ((Cell)new Cell().setBorder(null)).add((IBlockElement)((Paragraph)((Paragraph)new Paragraph(school.getSchoolName()).setTextAlignment(TextAlignment.CENTER)).setFontSize(18.0f)).setFont(this.bold));
                    headerTable.addCell(schoolNameCell);
                }
                catch (Exception e) {
                    System.err.println("Error adding school logo: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("No school data or logo available");
            }
            Cell dividerCell = ((Cell)new Cell().setBorder(null)).add((IBlockElement)((Paragraph)((Paragraph)new Paragraph("").setHeight(2.0f)).setMarginTop(5.0f)).setMarginBottom(10.0f));
            headerTable.addCell(dividerCell);
            Cell titleCell = ((Cell)new Cell().setBorder(null)).add((IBlockElement)((Paragraph)((Paragraph)new Paragraph("Exam Routine").setTextAlignment(TextAlignment.CENTER)).setFontSize(22.0f)).setFont(this.bold));
            headerTable.addCell(titleCell);
            Cell infoCell = ((Cell)new Cell().setBorder(null)).add((IBlockElement)((Paragraph)((Paragraph)((Paragraph)new Paragraph("Class: " + className + "  |  Exam Type: " + examType).setTextAlignment(TextAlignment.CENTER)).setFontSize(14.0f)).setFont(this.italic)).setMarginBottom(15.0f));
            headerTable.addCell(infoCell);
            document.add((IBlockElement)headerTable);
            float[] columnWidths = new float[]{10.0f, 10.0f, 5.0f, 5.0f};
            Table table = new Table(columnWidths);
            table.setWidth(500.0f);
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);
            table.setBorder((Border)new SolidBorder(ColorConstants.BLACK, 1.0f));
            table.addHeaderCell(this.createHeaderCell("Date"));
            table.addHeaderCell(this.createHeaderCell("Subject"));
            table.addHeaderCell(this.createHeaderCell("Start Time"));
            table.addHeaderCell(this.createHeaderCell("End Time"));
            boolean isAlternateRow = false;
            for (ExamRoutine routine : routines) {
                for (ExamRoutine.SubjectExam subjectExam : routine.getSubjects()) {
                    table.addCell(this.createDataCell(subjectExam.getExamDate(), isAlternateRow));
                    table.addCell(this.createDataCell(subjectExam.getSubjectName(), isAlternateRow));
                    table.addCell(this.createDataCell(subjectExam.getStartTime(), isAlternateRow));
                    table.addCell(this.createDataCell(subjectExam.getEndTime(), isAlternateRow));
                    isAlternateRow = !isAlternateRow;
                }
            }
            document.add((IBlockElement)table);
            Table footerTable = new Table(1).useAllAvailableWidth();
            footerTable.setMarginTop(20.0f);
            Cell footerCell = ((Cell)new Cell().setBorder(null)).add((IBlockElement)((Paragraph)((Paragraph)new Paragraph("Generated on: " + LocalDate.now().toString()).setTextAlignment(TextAlignment.CENTER)).setFontSize(10.0f)).setFont(this.italic));
            footerTable.addCell(footerCell);
            document.add((IBlockElement)footerTable);
            document.close();
            object = out.toByteArray();
        }
        catch (Throwable throwable) {
            try {
                try {
                    out.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception e) {
                throw new RuntimeException("Error generating PDF", e);
            }
        }
        try {
            out.close();
        } catch (IOException ignored) {}
        return object;
    }

    private Cell createHeaderCell(String content) {
        Cell cell = (Cell)((Cell)((Cell)new Cell().add((IBlockElement)((Paragraph)new Paragraph(content).setTextAlignment(TextAlignment.CENTER)).setFont(this.bold)).setBackgroundColor((Color)new DeviceRgb(63, 81, 181))).setFontColor(ColorConstants.WHITE)).setPadding(8.0f);
        return cell;
    }

    private Cell createDataCell(String content, boolean isAlternate) {
        Cell cell = (Cell)new Cell().add((IBlockElement)new Paragraph(content).setTextAlignment(TextAlignment.CENTER)).setPadding(8.0f);
        if (isAlternate) {
            cell.setBackgroundColor((Color)new DeviceRgb(237, 237, 237));
        }
        return cell;
    }

    public ExamRoutine createRoutineWithSchool(ExamRoutine routine, String schoolId) {
        routine.setSchoolId(schoolId);
        boolean routineExists = this.repository.existsByClassNameAndExamTypeAndSchoolId(routine.getClassName(), routine.getExamType(), schoolId);
        if (routineExists) {
            throw new RuntimeException("Exam routine for this class already exists in this school.");
        }
        return (ExamRoutine)this.repository.save(routine);
    }

    public ResponseMessage updateRoutineWithSchool(String id, ExamRoutine updatedRoutine, String schoolId) {
        boolean routineExists;
        updatedRoutine.setSchoolId(schoolId);
        Optional existingRoutineOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (existingRoutineOpt.isEmpty()) {
            return new ResponseMessage("Exam routine not found with ID: " + id + " for this school", null);
        }
        ExamRoutine existingRoutine = (ExamRoutine)existingRoutineOpt.get();
        if (!(updatedRoutine.getClassName().equals(existingRoutine.getClassName()) && updatedRoutine.getExamType().equals(existingRoutine.getExamType()) || !(routineExists = this.repository.existsByClassNameAndExamTypeAndSchoolId(updatedRoutine.getClassName(), updatedRoutine.getExamType(), schoolId)))) {
            return new ResponseMessage("Exam routine already exists for this class and exam type in this school", null);
        }
        if (updatedRoutine.getClassName() != null && !updatedRoutine.getClassName().isEmpty()) {
            existingRoutine.setClassName(updatedRoutine.getClassName());
        }
        if (updatedRoutine.getExamType() != null && !updatedRoutine.getExamType().isEmpty()) {
            existingRoutine.setExamType(updatedRoutine.getExamType());
        }
        if (updatedRoutine.getSubjects() != null) {
            if (updatedRoutine.getSubjects().isEmpty()) {
                existingRoutine.getSubjects().clear();
            } else {
                existingRoutine.getSubjects().clear();
                existingRoutine.getSubjects().addAll(updatedRoutine.getSubjects());
            }
        }
        ExamRoutine savedRoutine = (ExamRoutine)this.repository.save(existingRoutine);
        return new ResponseMessage("Exam routine updated successfully for school", savedRoutine);
    }

    public List<ExamRoutine> getAllRoutinesBySchool(String schoolId) {
        return this.repository.findBySchoolId(schoolId);
    }

    public List<ExamRoutine> getRoutineByClassAndSchool(String className, String examType, String schoolId) {
        return this.repository.findByClassNameAndExamTypeAndSchoolId(className, examType, schoolId);
    }

    public boolean deleteRoutineWithSchool(String id, String schoolId) {
        Optional routineOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (routineOpt.isPresent()) {
            this.repository.deleteById(id);
            return true;
        }
        return false;
    }

    public byte[] generateRoutinePdfWithSchool(String className, String examType, String schoolId) {
        List routines = this.repository.findByClassNameAndExamTypeAndSchoolId(className, examType, schoolId);
        if (routines.isEmpty()) {
            throw new RuntimeException("No routines found for Class: " + className + " this ExamType " + examType + " in this school");
        }
        return this.generatePdf(routines, className, examType);
    }
}

