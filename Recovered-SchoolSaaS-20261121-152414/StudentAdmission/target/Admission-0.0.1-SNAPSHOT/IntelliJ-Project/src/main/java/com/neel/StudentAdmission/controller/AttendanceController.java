/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.controller.AttendanceController
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.AttendanceDetails
 *  com.neel.StudentAdmission.service.AttendanceService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.format.annotation.DateTimeFormat
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.StudentAdmission.controller;

import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.AttendanceDetails;
import com.neel.StudentAdmission.service.AttendanceService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping
public class AttendanceController {
    @Autowired
    private AttendanceService attendanceService;

    @GetMapping(value={"/attendance/students"})
    public ResponseEntity<List<Admission>> getStudentsByClassAndSection(@RequestParam String className, @RequestParam String section) {
        List students = this.attendanceService.getStudentsByClassAndSection(className, section);
        return ResponseEntity.ok((Object)students);
    }

    @GetMapping(value={"/{schoolId}/attendance/students"})
    public ResponseEntity<List<Admission>> getStudentsByClassSectionAndSchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section) {
        List students = this.attendanceService.getStudentsByClassSectionAndSchool(className, section, schoolId);
        return ResponseEntity.ok((Object)students);
    }

    @GetMapping(value={"/attendance/record"})
    public ResponseEntity<List<AttendanceDetails>> getAttendanceRecords(@RequestParam String className, @RequestParam String section, @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date attendanceDate) {
        List attendanceRecords = attendanceDate != null ? this.attendanceService.getAttendanceByClassSectionAndDate(className, section, attendanceDate) : this.attendanceService.getAttendanceByClassAndSection(className, section);
        return ResponseEntity.ok((Object)attendanceRecords);
    }

    @GetMapping(value={"/attendance/record/{schoolId}"})
    public ResponseEntity<List<AttendanceDetails>> getAttendanceRecordsBySchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section, @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date attendanceDate) {
        List attendanceRecords = attendanceDate != null ? this.attendanceService.getAttendanceByClassSectionDateAndSchool(className, section, attendanceDate, schoolId) : this.attendanceService.getAttendanceByClassSectionAndSchool(className, section, schoolId);
        return ResponseEntity.ok((Object)attendanceRecords);
    }

    @PostMapping(value={"/attendance/record"})
    public ResponseEntity<String> recordAttendance(@RequestParam String className, @RequestParam String section, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate attendanceDate, @RequestBody Map<String, String> attendance) {
        Date convertedDate = Date.from(attendanceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String response = this.attendanceService.saveAttendance(className, section, convertedDate, attendance);
        return ResponseEntity.ok((Object)response);
    }

    @PostMapping(value={"/{schoolId}/attendance/record"})
    public ResponseEntity<String> recordAttendanceWithSchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate attendanceDate, @RequestBody Map<String, String> attendance) {
        Date convertedDate = Date.from(attendanceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String response = this.attendanceService.saveAttendanceWithSchool(className, section, convertedDate, attendance, schoolId);
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/attendance/student-attendance"})
    public ResponseEntity<List<Map<String, String>>> getStudentAttendance(@RequestParam String className, @RequestParam String section, @RequestParam String month) {
        Date[] dateRange = this.getStartEndDateOfMonth(month);
        if (dateRange == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Date startDate = dateRange[0];
        Date endDate = dateRange[1];
        Map attendanceData = this.attendanceService.getStudentAttendanceByClassAndSection(className, section, startDate, endDate);
        List responseArray = this.convertAttendanceToArray(attendanceData);
        return ResponseEntity.ok((Object)responseArray);
    }

    @GetMapping(value={"/{schoolId}/attendance/student-attendance"})
    public ResponseEntity<List<Map<String, String>>> getStudentAttendanceWithSchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section, @RequestParam String month) {
        Date[] dateRange = this.getStartEndDateOfMonth(month);
        if (dateRange == null) {
            return ResponseEntity.badRequest().body(null);
        }
        Date startDate = dateRange[0];
        Date endDate = dateRange[1];
        Map attendanceData = this.attendanceService.getStudentAttendanceByClassSectionAndSchool(className, section, startDate, endDate, schoolId);
        List responseArray = this.convertAttendanceToArray(attendanceData);
        return ResponseEntity.ok((Object)responseArray);
    }

    private List<Map<String, String>> convertAttendanceToArray(Map<String, Map<String, String>> attendanceData) {
        ArrayList<Map<String, String>> attendanceList = new ArrayList<Map<String, String>>();
        for (Map.Entry<String, Map<String, String>> entry : attendanceData.entrySet()) {
            LinkedHashMap<String, String> studentAttendance = new LinkedHashMap<String, String>();
            studentAttendance.put("rollNo", entry.getKey());
            for (Map.Entry<String, String> dateEntry : entry.getValue().entrySet()) {
                studentAttendance.put(dateEntry.getKey(), dateEntry.getValue());
            }
            attendanceList.add(studentAttendance);
        }
        return attendanceList;
    }

    private Date[] getStartEndDateOfMonth(String month) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
            Date parsedMonth = dateFormat.parse(month);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(parsedMonth);
            startCalendar.set(5, 1);
            Date startDate = startCalendar.getTime();
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(parsedMonth);
            endCalendar.set(5, endCalendar.getActualMaximum(5));
            Date endDate = endCalendar.getTime();
            return new Date[]{startDate, endDate};
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(value={"/attendance/student-attendance1"})
    public ResponseEntity<List<Map<String, Object>>> getFormattedStudentAttendance(@RequestParam String className, @RequestParam String section, @RequestParam String month) {
        List response = this.attendanceService.getFormattedAttendanceByClassAndSection(className, section, month);
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/{schoolId}/attendance/student-attendance1"})
    public ResponseEntity<List<Map<String, Object>>> getFormattedStudentAttendanceWithSchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section, @RequestParam String month) {
        List response = this.attendanceService.getFormattedAttendanceByClassSectionAndSchool(className, section, month, schoolId);
        return ResponseEntity.ok((Object)response);
    }
}

