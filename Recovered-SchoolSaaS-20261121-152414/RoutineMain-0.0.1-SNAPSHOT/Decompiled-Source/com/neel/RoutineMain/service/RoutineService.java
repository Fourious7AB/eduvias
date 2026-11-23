/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.databind.JsonNode
 *  com.fasterxml.jackson.databind.ObjectMapper
 *  com.itextpdf.io.image.ImageData
 *  com.itextpdf.io.image.ImageDataFactory
 *  com.itextpdf.kernel.colors.Color
 *  com.itextpdf.kernel.colors.ColorConstants
 *  com.itextpdf.kernel.colors.DeviceRgb
 *  com.itextpdf.kernel.font.PdfFont
 *  com.itextpdf.kernel.font.PdfFontFactory
 *  com.itextpdf.kernel.geom.PageSize
 *  com.itextpdf.kernel.pdf.PdfDocument
 *  com.itextpdf.kernel.pdf.PdfWriter
 *  com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer
 *  com.itextpdf.kernel.pdf.canvas.draw.SolidLine
 *  com.itextpdf.layout.Document
 *  com.itextpdf.layout.borders.Border
 *  com.itextpdf.layout.borders.SolidBorder
 *  com.itextpdf.layout.element.Cell
 *  com.itextpdf.layout.element.IBlockElement
 *  com.itextpdf.layout.element.Image
 *  com.itextpdf.layout.element.LineSeparator
 *  com.itextpdf.layout.element.Paragraph
 *  com.itextpdf.layout.element.Table
 *  com.itextpdf.layout.properties.HorizontalAlignment
 *  com.itextpdf.layout.properties.TextAlignment
 *  com.itextpdf.layout.properties.UnitValue
 *  com.neel.RoutineMain.model.ClassTimePage
 *  com.neel.RoutineMain.model.ResponseMessage
 *  com.neel.RoutineMain.model.Routine
 *  com.neel.RoutineMain.model.Routine$Period
 *  com.neel.RoutineMain.model.School
 *  com.neel.RoutineMain.repo.ClassTimePageRepository
 *  com.neel.RoutineMain.repo.RoutineRepository
 *  com.neel.RoutineMain.service.RoutineService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestTemplate
 */
package com.neel.RoutineMain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.neel.RoutineMain.model.ClassTimePage;
import com.neel.RoutineMain.model.ResponseMessage;
import com.neel.RoutineMain.model.Routine;
import com.neel.RoutineMain.model.School;
import com.neel.RoutineMain.repo.ClassTimePageRepository;
import com.neel.RoutineMain.repo.RoutineRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.CallSite;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RoutineService {
    @Autowired
    private RoutineRepository repository;
    @Autowired
    private ClassTimePageRepository classTimePageRepository;
    @Autowired
    private RestTemplate restTemplate;
    PdfFont bold = PdfFontFactory.createFont((String)"Helvetica-Bold");
    PdfFont italic = PdfFontFactory.createFont((String)"Helvetica-Oblique");
    @Value(value="${School.Service.details.url}")
    private String schoolLogoUrl;

    public ResponseMessage createRoutine(Routine routine) {
        int totalPeriods = 9;
        ArrayList<Object> periods = new ArrayList<Object>(Collections.nCopies(totalPeriods, null));
        for (Routine.Period userPeriod : routine.getPeriods()) {
            int periodIndex = this.parsePeriodIndex(userPeriod.getPeriodName());
            if (periodIndex < 0 || periodIndex >= totalPeriods) {
                return new ResponseMessage("Invalid period name: " + userPeriod.getPeriodName(), null);
            }
            if (periods.get(periodIndex) != null) {
                return new ResponseMessage("Duplicate entry for period: " + userPeriod.getPeriodName(), null);
            }
            periods.set(periodIndex, userPeriod);
        }
        for (int i = 0; i < totalPeriods; ++i) {
            if (periods.get(i) != null) continue;
            periods.set(i, new Routine.Period(this.getPeriodName(i), "No Class", ""));
        }
        routine.setPeriods(periods);
        Routine savedRoutine = (Routine)this.repository.save((Object)routine);
        return new ResponseMessage("Routine created successfully", (Object)savedRoutine);
    }

    private int parsePeriodIndex(String periodName) {
        switch (periodName.toLowerCase()) {
            case "1st period": {
                return 0;
            }
            case "2nd period": {
                return 1;
            }
            case "3rd period": {
                return 2;
            }
            case "4th period": {
                return 3;
            }
            case "break period": {
                return 4;
            }
            case "5th period": {
                return 5;
            }
            case "6th period": {
                return 6;
            }
            case "7th period": {
                return 7;
            }
            case "8th period": {
                return 8;
            }
        }
        return -1;
    }

    private String getPeriodName(int index) {
        switch (index) {
            case 0: {
                return "1st period";
            }
            case 1: {
                return "2nd period";
            }
            case 2: {
                return "3rd period";
            }
            case 3: {
                return "4th period";
            }
            case 4: {
                return "break period";
            }
            case 5: {
                return "5th period";
            }
            case 6: {
                return "6th period";
            }
            case 7: {
                return "7th period";
            }
            case 8: {
                return "8th period";
            }
        }
        throw new IllegalArgumentException("Invalid index for period name: " + index);
    }

    public ResponseMessage updateRoutine(String id, Routine routine) {
        routine.setSchoolId("67ca9167d56ec91fc9539234");
        Optional existingRoutineOpt = this.repository.findById((Object)id);
        if (existingRoutineOpt.isEmpty()) {
            return new ResponseMessage("Routine not found with ID: " + id, null);
        }
        Routine existingRoutine = (Routine)existingRoutineOpt.get();
        int totalPeriods = 8;
        ArrayList<Object> periods = new ArrayList<Object>(Collections.nCopies(totalPeriods, null));
        for (Routine.Period userPeriod : routine.getPeriods()) {
            int periodIndex = this.parsePeriodIndex(userPeriod.getPeriodName());
            if (periodIndex < 0 || periodIndex >= totalPeriods) {
                return new ResponseMessage("Invalid period name: " + userPeriod.getPeriodName(), null);
            }
            if (periods.get(periodIndex) != null) {
                return new ResponseMessage("Duplicate entry for period: " + userPeriod.getPeriodName(), null);
            }
            periods.set(periodIndex, userPeriod);
        }
        for (int i = 0; i < totalPeriods; ++i) {
            if (periods.get(i) != null) continue;
            periods.set(i, new Routine.Period(this.getPeriodName(i), "No Class", ""));
        }
        existingRoutine.setClassName(routine.getClassName());
        existingRoutine.setSection(routine.getSection());
        existingRoutine.setDay(routine.getDay());
        existingRoutine.setPeriods(periods);
        existingRoutine.setSchoolId(routine.getSchoolId());
        Routine updatedRoutine = (Routine)this.repository.save((Object)existingRoutine);
        return new ResponseMessage("Routine updated successfully", (Object)updatedRoutine);
    }

    public List<Routine> getAllRoutines() {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.repository.findBySchoolId(schoolId);
    }

    public Routine getRoutineById(String id) {
        return this.repository.findById((Object)id).orElse(null);
    }

    public List<Routine> searchRoutineByClassAndSection(String className, String section) {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.repository.findByClassNameAndSectionAndSchoolId(className, section, schoolId);
    }

    public void deleteRoutine(String id) {
        this.repository.deleteById((Object)id);
    }

    public ResponseMessage createRoutineWithSchool(Routine routine, String schoolId) {
        boolean routineExists;
        routine.setSchoolId(schoolId);
        List existingRoutines = this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndDayIgnoreCaseAndSchoolId(routine.getClassName(), routine.getSection(), routine.getDay(), schoolId);
        boolean bl = routineExists = !existingRoutines.isEmpty();
        if (routineExists) {
            return new ResponseMessage("Routine already created for this day in this school", null);
        }
        int totalPeriods = 9;
        ArrayList<Object> periods = new ArrayList<Object>(Collections.nCopies(totalPeriods, null));
        for (Routine.Period userPeriod : routine.getPeriods()) {
            int periodIndex = this.parsePeriodIndex(userPeriod.getPeriodName());
            if (periodIndex < 0 || periodIndex >= totalPeriods) {
                return new ResponseMessage("Invalid period name: " + userPeriod.getPeriodName(), null);
            }
            if (periods.get(periodIndex) != null) {
                return new ResponseMessage("Duplicate entry for period: " + userPeriod.getPeriodName(), null);
            }
            periods.set(periodIndex, userPeriod);
        }
        for (int i = 0; i < totalPeriods; ++i) {
            if (periods.get(i) != null) continue;
            periods.set(i, new Routine.Period(this.getPeriodName(i), "No Class", ""));
        }
        routine.setPeriods(periods);
        Routine savedRoutine = (Routine)this.repository.save((Object)routine);
        return new ResponseMessage("Routine created successfully for school", (Object)savedRoutine);
    }

    public ResponseMessage updateRoutineWithSchool(String id, Routine routine, String schoolId) {
        routine.setSchoolId(schoolId);
        Optional existingRoutineOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (existingRoutineOpt.isEmpty()) {
            return new ResponseMessage("Routine not found with ID: " + id + " for this school", null);
        }
        Routine existingRoutine = (Routine)existingRoutineOpt.get();
        if (!(routine.getClassName().equals(existingRoutine.getClassName()) && routine.getSection().equals(existingRoutine.getSection()) && routine.getDay().equals(existingRoutine.getDay()))) {
            boolean routineExists;
            List existingRoutines = this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndDayIgnoreCaseAndSchoolId(routine.getClassName(), routine.getSection(), routine.getDay(), schoolId);
            boolean bl = routineExists = !existingRoutines.isEmpty();
            if (routineExists) {
                return new ResponseMessage("Routine already created for this day in this school", null);
            }
        }
        if (routine.getClassName() != null && !routine.getClassName().isEmpty()) {
            existingRoutine.setClassName(routine.getClassName());
        }
        if (routine.getSection() != null && !routine.getSection().isEmpty()) {
            existingRoutine.setSection(routine.getSection());
        }
        if (routine.getDay() != null && !routine.getDay().isEmpty()) {
            existingRoutine.setDay(routine.getDay());
        }
        if (routine.getPeriods() != null && !routine.getPeriods().isEmpty()) {
            List existingPeriods = existingRoutine.getPeriods();
            for (Routine.Period updatedPeriod : routine.getPeriods()) {
                int periodIndex = this.parsePeriodIndex(updatedPeriod.getPeriodName());
                if (periodIndex < 0 || periodIndex >= existingPeriods.size()) {
                    return new ResponseMessage("Invalid period name: " + updatedPeriod.getPeriodName(), null);
                }
                Routine.Period existingPeriod = (Routine.Period)existingPeriods.get(periodIndex);
                if (updatedPeriod.getTeacherName() != null && !updatedPeriod.getTeacherName().isEmpty()) {
                    existingPeriod.setTeacherName(updatedPeriod.getTeacherName());
                }
                if (updatedPeriod.getSubjectName() == null || updatedPeriod.getSubjectName().isEmpty()) continue;
                existingPeriod.setSubjectName(updatedPeriod.getSubjectName());
            }
        }
        Routine updatedRoutine = (Routine)this.repository.save((Object)existingRoutine);
        return new ResponseMessage("Routine updated successfully for school", (Object)updatedRoutine);
    }

    public List<Routine> getAllRoutinesBySchool(String schoolId) {
        return this.repository.findBySchoolId(schoolId);
    }

    public Routine getRoutineByIdAndSchool(String id, String schoolId) {
        return this.repository.findByIdAndSchoolId(id, schoolId).orElse(null);
    }

    public List<Routine> searchRoutineByClassAndSectionWithSchool(String className, String section, String schoolId) {
        return this.repository.findByClassNameAndSectionAndSchoolId(className, section, schoolId);
    }

    public boolean deleteRoutineWithSchool(String id, String schoolId) {
        Optional routineOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (routineOpt.isPresent()) {
            this.repository.deleteById((Object)id);
            return true;
        }
        return false;
    }

    public byte[] generateRoutinePdfWithSchool(String className, String section, String schoolId) throws IOException {
        byte[] byArray;
        List routines = this.repository.findByClassNameAndSectionAndSchoolId(className, section, schoolId);
        if (routines.isEmpty()) {
            System.err.println("No routines found for Class: " + className + ", Section: " + section + " in school: " + schoolId);
            throw new RuntimeException("No routines found for Class: " + className + ", Section: " + section + " in this school");
        }
        System.out.println("Found " + routines.size() + " routines for Class: " + className + ", Section: " + section);
        PdfFont bold = PdfFontFactory.createFont((String)"Helvetica-Bold");
        PdfFont italic = PdfFontFactory.createFont((String)"Helvetica-Oblique");
        School school = this.fetchSchoolDetails(schoolId);
        System.out.println("Fetched school details: " + school);
        List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        routines.sort(Comparator.comparingInt(routine -> daysOfWeek.indexOf(routine.getDay())));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter((OutputStream)baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());
            document.setMargins(20.0f, 20.0f, 20.0f, 20.0f);
            if (school.getSchoolLogo() != null && !school.getSchoolLogo().isEmpty()) {
                try {
                    System.out.println("Attempting to load school logo: " + school.getSchoolLogo());
                    ImageData imageData = ImageDataFactory.create((String)school.getSchoolLogo());
                    Image logo = (Image)new Image(imageData).scaleToFit(100.0f, 100.0f).setHorizontalAlignment(HorizontalAlignment.CENTER);
                    document.add(logo);
                    System.out.println("School logo added successfully");
                }
                catch (Exception e) {
                    System.err.println("Could not load school logo: " + e.getMessage());
                }
            }
            Paragraph schoolName = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph(school.getSchoolName()).setFontSize(22.0f)).setFont(bold)).setTextAlignment(TextAlignment.CENTER)).setFontColor(ColorConstants.DARK_GRAY);
            document.add((IBlockElement)schoolName);
            Paragraph schoolAddress = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph(school.getSchoolAddress()).setFontSize(12.0f)).setTextAlignment(TextAlignment.CENTER)).setFontColor(ColorConstants.DARK_GRAY)).setMarginBottom(5.0f);
            document.add((IBlockElement)schoolAddress);
            SolidLine line = new SolidLine(1.0f);
            line.setColor(ColorConstants.LIGHT_GRAY);
            LineSeparator ls = new LineSeparator((ILineDrawer)line);
            ls.setMarginTop(5.0f);
            ls.setMarginBottom(10.0f);
            document.add((IBlockElement)ls);
            Paragraph title = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph("Class Routine").setFontSize(18.0f)).setFont(bold)).setTextAlignment(TextAlignment.CENTER)).setFontColor(ColorConstants.BLUE);
            document.add((IBlockElement)title);
            Paragraph subtitle = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph("Class: " + className + " | Section: " + section).setFontSize(14.0f)).setTextAlignment(TextAlignment.CENTER)).setMarginBottom(15.0f)).setFontColor(ColorConstants.DARK_GRAY);
            document.add((IBlockElement)subtitle);
            Table table = (Table)((Table)new Table(UnitValue.createPercentArray((float[])new float[]{15.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f})).useAllAvailableWidth().setMarginTop(10.0f)).setMarginBottom(10.0f);
            table.setBorder((Border)new SolidBorder(ColorConstants.LIGHT_GRAY, 1.0f));
            table.setFontColor(ColorConstants.DARK_GRAY);
            table.addHeaderCell(this.createModernHeaderCell("Day/Period"));
            table.addHeaderCell(this.createModernHeaderCell("1st"));
            table.addHeaderCell(this.createModernHeaderCell("2nd"));
            table.addHeaderCell(this.createModernHeaderCell("3rd"));
            table.addHeaderCell(this.createModernHeaderCell("4th"));
            table.addHeaderCell(this.createModernHeaderCell("Break"));
            table.addHeaderCell(this.createModernHeaderCell("5th"));
            table.addHeaderCell(this.createModernHeaderCell("6th"));
            table.addHeaderCell(this.createModernHeaderCell("7th"));
            table.addHeaderCell(this.createModernHeaderCell("8th"));
            List classTimes = this.classTimePageRepository.findBySchoolId(schoolId);
            if (!classTimes.isEmpty()) {
                table.addCell(this.createModernSubHeaderCell("Time"));
                for (int i = 0; i < 9; ++i) {
                    Object timeText = "";
                    for (ClassTimePage classTime : classTimes) {
                        if (!classTime.getPeriods().equals(this.getPeriodName(i))) continue;
                        timeText = classTime.getStartTime() + " - " + classTime.getEndTime();
                        break;
                    }
                    table.addCell(this.createModernTimeCell((String)timeText));
                }
            }
            for (Routine routine2 : routines) {
                table.addCell(this.createModernDayCell(routine2.getDay()));
                for (int i = 0; i < 9; ++i) {
                    Routine.Period period = (Routine.Period)routine2.getPeriods().get(i);
                    String subjectText = period.getSubjectName();
                    String teacherText = period.getTeacherName();
                    if (teacherText.equals("No Class")) {
                        table.addCell(this.createModernEmptyCell());
                        continue;
                    }
                    table.addCell(this.createModernPeriodCell(subjectText, teacherText));
                }
            }
            document.add((IBlockElement)table);
            Table footerTable = new Table(2).useAllAvailableWidth();
            Cell contactCell = new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph("Contact: " + school.getSchoolPhone() + " | " + school.getSchoolEmail()).setFontSize(8.0f)).setTextAlignment(TextAlignment.LEFT)).setFontColor(ColorConstants.GRAY));
            contactCell.setBorder(Border.NO_BORDER);
            footerTable.addCell(contactCell);
            Cell timestampCell = new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph("Generated on: " + new Date().toString()).setFontSize(8.0f)).setTextAlignment(TextAlignment.RIGHT)).setFontColor(ColorConstants.GRAY));
            timestampCell.setBorder(Border.NO_BORDER);
            footerTable.addCell(timestampCell);
            document.add((IBlockElement)footerTable);
            document.close();
            byArray = baos.toByteArray();
        }
        catch (Throwable throwable) {
            try {
                try {
                    try {
                        baos.close();
                    }
                    catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                    throw throwable;
                }
                catch (Exception e) {
                    System.err.println("Error in PDF document creation: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error generating PDF document: " + e.getMessage(), e);
                }
            }
            catch (Exception e) {
                System.err.println("Error in generateRoutinePdfWithSchool: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
            }
        }
        baos.close();
        return byArray;
    }

    public byte[] generateTeacherRoutinePdfWithSchool(String teacherName, String schoolId) throws IOException {
        byte[] byArray;
        List allRoutines = this.repository.findBySchoolId(schoolId);
        School school = this.fetchSchoolDetails(schoolId);
        PdfFont bold = PdfFontFactory.createFont((String)"Helvetica-Bold");
        PdfFont italic = PdfFontFactory.createFont((String)"Helvetica-Oblique");
        HashMap teacherRoutines = new HashMap();
        for (Routine routine : allRoutines) {
            String className = routine.getClassName();
            String section = routine.getSection();
            String day = routine.getDay();
            String classKey = className + " " + section;
            for (int i = 0; i < routine.getPeriods().size(); ++i) {
                Routine.Period period = (Routine.Period)routine.getPeriods().get(i);
                if (!period.getTeacherName().equalsIgnoreCase(teacherName)) continue;
                teacherRoutines.putIfAbsent((CallSite)((Object)classKey), new HashMap());
                ((Map)teacherRoutines.get(classKey)).putIfAbsent(day, new ArrayList());
                HashMap<String, String> periodDetails = new HashMap<String, String>();
                periodDetails.put("period", period.getPeriodName());
                periodDetails.put("subject", period.getSubjectName());
                periodDetails.put("periodIndex", String.valueOf(i));
                ((List)((Map)teacherRoutines.get(classKey)).get(day)).add(periodDetails);
            }
        }
        if (teacherRoutines.isEmpty()) {
            throw new RuntimeException("No routines found for teacher: " + teacherName + " in this school");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter writer = new PdfWriter((OutputStream)baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());
            document.setMargins(20.0f, 20.0f, 20.0f, 20.0f);
            if (school.getSchoolLogo() != null && !school.getSchoolLogo().isEmpty()) {
                try {
                    ImageData imageData = ImageDataFactory.create((String)school.getSchoolLogo());
                    Image logo = (Image)new Image(imageData).scaleToFit(100.0f, 100.0f).setHorizontalAlignment(HorizontalAlignment.CENTER);
                    document.add(logo);
                }
                catch (Exception e) {
                    System.err.println("Could not load school logo: " + e.getMessage());
                }
            }
            Paragraph schoolName = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph(school.getSchoolName()).setFontSize(22.0f)).setFont(bold)).setTextAlignment(TextAlignment.CENTER)).setFontColor(ColorConstants.DARK_GRAY);
            document.add((IBlockElement)schoolName);
            Paragraph schoolAddress = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph(school.getSchoolAddress()).setFontSize(12.0f)).setTextAlignment(TextAlignment.CENTER)).setFontColor(ColorConstants.DARK_GRAY)).setMarginBottom(5.0f);
            document.add((IBlockElement)schoolAddress);
            SolidLine line = new SolidLine(1.0f);
            line.setColor(ColorConstants.LIGHT_GRAY);
            LineSeparator ls = new LineSeparator((ILineDrawer)line);
            ls.setMarginTop(5.0f);
            ls.setMarginBottom(10.0f);
            document.add((IBlockElement)ls);
            Paragraph title = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph("Teacher Routine").setFontSize(18.0f)).setFont(bold)).setTextAlignment(TextAlignment.CENTER)).setFontColor(ColorConstants.BLUE);
            document.add((IBlockElement)title);
            Paragraph subtitle = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph("Teacher: " + teacherName).setFontSize(14.0f)).setTextAlignment(TextAlignment.CENTER)).setMarginBottom(15.0f)).setFontColor(ColorConstants.DARK_GRAY);
            document.add((IBlockElement)subtitle);
            List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
            Table table = (Table)((Table)new Table(UnitValue.createPercentArray((float[])new float[]{15.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f, 9.0f})).useAllAvailableWidth().setMarginTop(10.0f)).setMarginBottom(10.0f);
            table.setBorder((Border)new SolidBorder(ColorConstants.LIGHT_GRAY, 1.0f));
            table.setFontColor(ColorConstants.DARK_GRAY);
            table.addHeaderCell(this.createModernHeaderCell("Day/Period"));
            table.addHeaderCell(this.createModernHeaderCell("1st"));
            table.addHeaderCell(this.createModernHeaderCell("2nd"));
            table.addHeaderCell(this.createModernHeaderCell("3rd"));
            table.addHeaderCell(this.createModernHeaderCell("4th"));
            table.addHeaderCell(this.createModernHeaderCell("Break"));
            table.addHeaderCell(this.createModernHeaderCell("5th"));
            table.addHeaderCell(this.createModernHeaderCell("6th"));
            table.addHeaderCell(this.createModernHeaderCell("7th"));
            table.addHeaderCell(this.createModernHeaderCell("8th"));
            List classTimes = this.classTimePageRepository.findBySchoolId(schoolId);
            if (!classTimes.isEmpty()) {
                table.addCell(this.createModernSubHeaderCell("Time"));
                for (int i = 0; i < 9; ++i) {
                    Object timeText = "";
                    for (ClassTimePage classTime : classTimes) {
                        if (!classTime.getPeriods().equals(this.getPeriodName(i))) continue;
                        timeText = classTime.getStartTime() + " - " + classTime.getEndTime();
                        break;
                    }
                    table.addCell(this.createModernTimeCell((String)timeText));
                }
            }
            for (String day : daysOfWeek) {
                table.addCell(this.createModernDayCell(day));
                for (int periodIndex = 0; periodIndex < 9; ++periodIndex) {
                    Object classInfo = "";
                    Object subjectInfo = "";
                    for (Map.Entry classEntry : teacherRoutines.entrySet()) {
                        String classKey = (String)classEntry.getKey();
                        Map dayMap = (Map)classEntry.getValue();
                        if (!dayMap.containsKey(day)) continue;
                        for (Map periodDetails : (List)dayMap.get(day)) {
                            if (Integer.parseInt((String)periodDetails.get("periodIndex")) != periodIndex) continue;
                            if (!((String)classInfo).isEmpty()) {
                                classInfo = (String)classInfo + "\n";
                                subjectInfo = (String)subjectInfo + "\n";
                            }
                            classInfo = (String)classInfo + classKey;
                            subjectInfo = (String)subjectInfo + (String)periodDetails.get("subject");
                        }
                    }
                    if (((String)classInfo).isEmpty()) {
                        table.addCell(this.createModernEmptyCell());
                        continue;
                    }
                    table.addCell(this.createModernTeacherPeriodCell((String)classInfo, (String)subjectInfo));
                }
            }
            document.add((IBlockElement)table);
            Table footerTable = new Table(2).useAllAvailableWidth();
            Cell contactCell = new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph("Contact: " + school.getSchoolPhone() + " | " + school.getSchoolEmail()).setFontSize(8.0f)).setTextAlignment(TextAlignment.LEFT)).setFontColor(ColorConstants.GRAY));
            contactCell.setBorder(Border.NO_BORDER);
            footerTable.addCell(contactCell);
            Cell timestampCell = new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph("Generated on: " + new Date().toString()).setFontSize(8.0f)).setTextAlignment(TextAlignment.RIGHT)).setFontColor(ColorConstants.GRAY));
            timestampCell.setBorder(Border.NO_BORDER);
            footerTable.addCell(timestampCell);
            document.add((IBlockElement)footerTable);
            document.close();
            byArray = baos.toByteArray();
        }
        catch (Throwable throwable) {
            try {
                try {
                    baos.close();
                }
                catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            }
            catch (Exception e) {
                throw new RuntimeException("Error generating teacher routine PDF: " + e.getMessage(), e);
            }
        }
        baos.close();
        return byArray;
    }

    private Cell createModernHeaderCell(String text) throws IOException {
        PdfFont bold = PdfFontFactory.createFont((String)"Helvetica-Bold");
        PdfFont italic = PdfFontFactory.createFont((String)"Helvetica-Oblique");
        Cell cell = (Cell)((Cell)((Cell)new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph(text).setTextAlignment(TextAlignment.CENTER)).setFont(bold)).setFontColor(ColorConstants.WHITE)).setBackgroundColor((Color)new DeviceRgb(41, 128, 185))).setPadding(7.0f)).setBorder((Border)new SolidBorder(ColorConstants.WHITE, 0.5f));
        return cell;
    }

    private Cell createModernSubHeaderCell(String text) throws IOException {
        PdfFont bold = PdfFontFactory.createFont((String)"Helvetica-Bold");
        Cell cell = (Cell)((Cell)((Cell)new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph(text).setTextAlignment(TextAlignment.CENTER)).setFont(bold)).setFontColor(ColorConstants.WHITE)).setBackgroundColor((Color)new DeviceRgb(52, 152, 219))).setPadding(5.0f)).setBorder((Border)new SolidBorder(ColorConstants.WHITE, 0.5f));
        return cell;
    }

    private Cell createModernTimeCell(String text) throws IOException {
        PdfFont italic = PdfFontFactory.createFont((String)"Helvetica-Oblique");
        Cell cell = (Cell)((Cell)((Cell)new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph(text).setTextAlignment(TextAlignment.CENTER)).setFontSize(8.0f)).setFont(italic)).setBackgroundColor((Color)new DeviceRgb(236, 240, 241))).setPadding(5.0f)).setBorder((Border)new SolidBorder(ColorConstants.WHITE, 0.5f));
        return cell;
    }

    private Cell createModernDayCell(String text) throws IOException {
        PdfFont bold = PdfFontFactory.createFont((String)"Helvetica-Bold");
        Cell cell = (Cell)((Cell)((Cell)new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph(text).setTextAlignment(TextAlignment.CENTER)).setFont(bold)).setFontColor(ColorConstants.WHITE)).setBackgroundColor((Color)new DeviceRgb(52, 73, 94))).setPadding(5.0f)).setBorder((Border)new SolidBorder(ColorConstants.WHITE, 0.5f));
        return cell;
    }

    private Cell createModernPeriodCell(String subject, String teacher) throws IOException {
        PdfFont bold = PdfFontFactory.createFont((String)"Helvetica-Bold");
        PdfFont italic = PdfFontFactory.createFont((String)"Helvetica-Oblique");
        Cell cell = (Cell)new Cell().setPadding(5.0f);
        cell.setBackgroundColor((Color)new DeviceRgb(245, 245, 245));
        Paragraph subjectPara = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph(subject).setTextAlignment(TextAlignment.CENTER)).setFont(bold)).setFontSize(10.0f)).setFontColor((Color)new DeviceRgb(44, 62, 80));
        Paragraph teacherPara = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph("(" + teacher + ")").setTextAlignment(TextAlignment.CENTER)).setFontSize(8.0f)).setFont(italic)).setFontColor((Color)new DeviceRgb(127, 140, 141));
        cell.add((IBlockElement)subjectPara).add((IBlockElement)teacherPara);
        cell.setBorder((Border)new SolidBorder(ColorConstants.WHITE, 0.5f));
        return cell;
    }

    private Cell createModernTeacherPeriodCell(String classInfo, String subject) throws IOException {
        PdfFont bold = PdfFontFactory.createFont((String)"Helvetica-Bold");
        Cell cell = (Cell)new Cell().setPadding(5.0f);
        cell.setBackgroundColor((Color)new DeviceRgb(245, 245, 245));
        Paragraph classPara = (Paragraph)((Paragraph)((Paragraph)((Paragraph)new Paragraph(classInfo).setTextAlignment(TextAlignment.CENTER)).setFont(bold)).setFontSize(10.0f)).setFontColor((Color)new DeviceRgb(44, 62, 80));
        Paragraph subjectPara = (Paragraph)((Paragraph)((Paragraph)new Paragraph(subject).setTextAlignment(TextAlignment.CENTER)).setFontSize(8.0f)).setFontColor((Color)new DeviceRgb(127, 140, 141));
        cell.add((IBlockElement)classPara).add((IBlockElement)subjectPara);
        cell.setBorder((Border)new SolidBorder(ColorConstants.WHITE, 0.5f));
        return cell;
    }

    private Cell createModernEmptyCell() {
        Cell cell = (Cell)((Cell)((Cell)new Cell().add((IBlockElement)((Paragraph)((Paragraph)new Paragraph("No Class").setTextAlignment(TextAlignment.CENTER)).setFontColor((Color)new DeviceRgb(189, 195, 199))).setFontSize(8.0f)).setBackgroundColor((Color)new DeviceRgb(250, 250, 250))).setPadding(5.0f)).setBorder((Border)new SolidBorder(ColorConstants.WHITE, 0.5f));
        return cell;
    }

    private Cell createCell(String text, boolean isHeader) {
        Cell cell = new Cell().add((IBlockElement)new Paragraph(text).setTextAlignment(TextAlignment.CENTER));
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
        }
        return cell;
    }

    public ResponseMessage getTeacherSpecificRoutinesWithSchool(String teacherName, String schoolId) {
        List allRoutines = this.repository.findBySchoolId(schoolId);
        HashMap teacherRoutines = new HashMap();
        for (Routine routine : allRoutines) {
            String className = routine.getClassName();
            String section = routine.getSection();
            String day = routine.getDay();
            String classKey = className + " " + section;
            for (Routine.Period period : routine.getPeriods()) {
                if (!period.getTeacherName().equalsIgnoreCase(teacherName)) continue;
                teacherRoutines.putIfAbsent((CallSite)((Object)classKey), new HashMap());
                ((Map)teacherRoutines.get(classKey)).putIfAbsent(day, new ArrayList());
                HashMap<String, String> periodDetails = new HashMap<String, String>();
                periodDetails.put("period", period.getPeriodName());
                periodDetails.put("subject", period.getSubjectName());
                ((List)((Map)teacherRoutines.get(classKey)).get(day)).add(periodDetails);
            }
        }
        if (teacherRoutines.isEmpty()) {
            return new ResponseMessage("No routines found for teacher: " + teacherName + " in this school", null);
        }
        return new ResponseMessage("Teacher routines retrieved successfully for school", teacherRoutines);
    }

    private School fetchSchoolDetails(String schoolId) {
        try {
            System.out.println("Fetching school details for schoolId: " + schoolId);
            String apiUrl = this.schoolLogoUrl + schoolId;
            System.out.println("Trying API URL: " + apiUrl);
            try {
                ResponseEntity response = this.restTemplate.getForEntity(new URI(apiUrl), String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree((String)response.getBody());
                    System.out.println("API response received: " + (String)response.getBody());
                    JsonNode dataNode = root.path("data");
                    if (dataNode.isMissingNode()) {
                        System.out.println("Data node is missing in the response");
                        dataNode = root;
                    }
                    School school = new School();
                    school.setSchoolId(schoolId);
                    school.setSchoolName(dataNode.path("schoolName").asText(dataNode.path("name").asText("Default School Name")));
                    school.setSchoolAddress(dataNode.path("schoolAddress").asText(dataNode.path("address").asText("Default Address")));
                    school.setSchoolPhone(dataNode.path("schoolPhone").asText(dataNode.path("phone").asText("Default Phone")));
                    school.setSchoolEmail(dataNode.path("schoolEmail").asText(dataNode.path("email").asText("default@example.com")));
                    String logoUrl = dataNode.path("schoolLogo").asText(dataNode.path("logoUrl").asText(""));
                    if (logoUrl != null && !logoUrl.isEmpty()) {
                        try {
                            new URI(logoUrl);
                            school.setSchoolLogo(logoUrl);
                            System.out.println("Valid logo URL found: " + logoUrl);
                        }
                        catch (Exception e) {
                            System.err.println("Invalid logo URL format: " + logoUrl);
                            school.setSchoolLogo("");
                        }
                    } else {
                        System.out.println("No logo URL found in response");
                        school.setSchoolLogo("");
                    }
                    return school;
                }
                System.err.println("API returned non-success status: " + response.getStatusCode());
            }
            catch (Exception e) {
                System.err.println("Error with API call: " + e.getMessage());
            }
            System.out.println("Using default school information as API call failed");
            School defaultSchool = new School();
            defaultSchool.setSchoolId(schoolId);
            defaultSchool.setSchoolName("School Name");
            defaultSchool.setSchoolAddress("School Address");
            defaultSchool.setSchoolPhone("School Phone");
            defaultSchool.setSchoolEmail("school@example.com");
            defaultSchool.setSchoolLogo("");
            return defaultSchool;
        }
        catch (Exception e) {
            System.err.println("Error fetching school details: " + e.getMessage());
            e.printStackTrace();
            School defaultSchool = new School();
            defaultSchool.setSchoolId(schoolId);
            defaultSchool.setSchoolName("School Name");
            defaultSchool.setSchoolAddress("School Address");
            defaultSchool.setSchoolPhone("School Phone");
            defaultSchool.setSchoolEmail("school@example.com");
            defaultSchool.setSchoolLogo("");
            return defaultSchool;
        }
    }
}

