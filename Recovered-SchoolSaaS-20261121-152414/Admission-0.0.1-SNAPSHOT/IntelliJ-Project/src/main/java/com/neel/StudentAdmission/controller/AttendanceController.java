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
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        return ResponseEntity.ok(students);
    }

    @GetMapping(value={"/{schoolId}/attendance/students"})
    public ResponseEntity<List<Admission>> getStudentsByClassSectionAndSchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section) {
        List students = this.attendanceService.getStudentsByClassSectionAndSchool(className, section, schoolId);
        return ResponseEntity.ok(students);
    }

    @GetMapping(value={"/attendance/record"})
    public ResponseEntity<List<AttendanceDetails>> getAttendanceRecords(@RequestParam String className, @RequestParam String section, @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date attendanceDate) {
        List attendanceRecords = attendanceDate != null ? this.attendanceService.getAttendanceByClassSectionAndDate(className, section, attendanceDate) : this.attendanceService.getAttendanceByClassAndSection(className, section);
        return ResponseEntity.ok(attendanceRecords);
    }

    @GetMapping(value={"/attendance/record/{schoolId}"})
    public ResponseEntity<List<AttendanceDetails>> getAttendanceRecordsBySchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section, @RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date attendanceDate) {
        List attendanceRecords = attendanceDate != null ? this.attendanceService.getAttendanceByClassSectionDateAndSchool(className, section, attendanceDate, schoolId) : this.attendanceService.getAttendanceByClassSectionAndSchool(className, section, schoolId);
        return ResponseEntity.ok(attendanceRecords);
    }

    @PostMapping(value={"/attendance/record"})
    public ResponseEntity<String> recordAttendance(@RequestParam String className, @RequestParam String section, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate attendanceDate, @RequestBody Map<String, String> attendance) {
        Date convertedDate = Date.from(attendanceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List existingAttendance = this.attendanceService.getAttendanceByClassSectionAndDate(className, section, convertedDate);
        if (!existingAttendance.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body("Attendance already recorded for this class, section, and date.");
        }
        String response = this.attendanceService.saveAttendance(className, section, convertedDate, attendance);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value={"/{schoolId}/attendance/record"})
    public ResponseEntity<String> recordAttendanceWithSchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate attendanceDate, @RequestBody Map<String, String> attendance) {
        Date convertedDate = Date.from(attendanceDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List existingAttendance = this.attendanceService.getAttendanceByClassSectionDateAndSchool(className, section, convertedDate, schoolId);
        if (!existingAttendance.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body("Attendance already recorded for this class, section, and date.");
        }
        String response = this.attendanceService.saveAttendanceWithSchool(className, section, convertedDate, attendance, schoolId);
        return ResponseEntity.ok(response);
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
        return ResponseEntity.ok(responseArray);
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
        return ResponseEntity.ok(responseArray);
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
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/{schoolId}/attendance/student-attendance1"})
    public ResponseEntity<List<Map<String, Object>>> getFormattedStudentAttendanceWithSchool(@PathVariable String schoolId, @RequestParam String className, @RequestParam String section, @RequestParam String month) {
        List response = this.attendanceService.getFormattedAttendanceByClassSectionAndSchool(className, section, month, schoolId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/attendance/calculate/custom/{schoolId}"})
    public ResponseEntity<Map<String, Object>> calculateCustomAttendance(@PathVariable String schoolId, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {
        Map attendanceStats = this.attendanceService.calculateAttendanceStatsBySchool(schoolId, startDate, endDate);
        return ResponseEntity.ok(attendanceStats);
    }

    @GetMapping(value={"/attendance/calculate/current-week/{schoolId}"})
    public ResponseEntity<Map<String, Object>> calculateCurrentWeekAttendance(@PathVariable String schoolId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(7, 2);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        Date startDate = calendar.getTime();
        calendar.add(7, 6);
        calendar.set(11, 23);
        calendar.set(12, 59);
        calendar.set(13, 59);
        calendar.set(14, 999);
        Date endDate = calendar.getTime();
        Map attendanceStats = this.attendanceService.calculateAttendanceStatsBySchool(schoolId, startDate, endDate);
        Map dayWiseStats = this.attendanceService.calculateDayWiseAttendance(schoolId, startDate, endDate);
        attendanceStats.put("dayWiseStats", dayWiseStats);
        return ResponseEntity.ok(attendanceStats);
    }

    @GetMapping(value={"/attendance/calculate/current-month/{schoolId}"})
    public ResponseEntity<Map<String, Object>> calculateCurrentMonthAttendance(@PathVariable String schoolId) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(2) + 1;
        int currentYear = calendar.get(1);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.clear();
        startCalendar.set(1, currentYear);
        startCalendar.set(2, currentMonth - 1);
        startCalendar.set(5, 1);
        startCalendar.set(11, 0);
        startCalendar.set(12, 0);
        startCalendar.set(13, 0);
        startCalendar.set(14, 0);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.clear();
        endCalendar.set(1, currentYear);
        endCalendar.set(2, currentMonth - 1);
        endCalendar.set(5, endCalendar.getActualMaximum(5));
        endCalendar.set(11, 23);
        endCalendar.set(12, 59);
        endCalendar.set(13, 59);
        endCalendar.set(14, 999);
        Date endDate = endCalendar.getTime();
        Map attendanceStats = this.attendanceService.calculateAttendanceStatsBySchool(schoolId, startDate, endDate);
        Map dateWiseStats = this.attendanceService.calculateDateWiseAttendance(schoolId, startDate, endDate);
        attendanceStats.put("dateWiseStats", dateWiseStats);
        return ResponseEntity.ok(attendanceStats);
    }

    @GetMapping(value={"/attendance/calculate/current-year/{schoolId}"})
    public ResponseEntity<Map<String, Object>> calculateCurrentYearAttendance(@PathVariable String schoolId) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(1);
        Map attendanceStats = this.attendanceService.calculateYearlyAttendance(schoolId, currentYear);
        Map monthWiseStats = this.attendanceService.calculateMonthWiseAttendance(schoolId, currentYear);
        attendanceStats.put("monthWiseStats", monthWiseStats);
        return ResponseEntity.ok(attendanceStats);
    }

    @GetMapping(value={"/attendance/student/{schoolId}/{rollNo}/custom"})
    public ResponseEntity<Map<String, Object>> calculateStudentCustomAttendance(@PathVariable String schoolId, @PathVariable String rollNo, @RequestParam String className, @RequestParam String section, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate) {
        Map attendanceStats = this.attendanceService.calculateStudentAttendance(schoolId, rollNo, className, section, startDate, endDate);
        return ResponseEntity.ok(attendanceStats);
    }

    @GetMapping(value={"/attendance/student/current-month/{schoolId}/{rollNo}"})
    public ResponseEntity<Map<String, Object>> calculateStudentCurrentMonthAttendance(@PathVariable String schoolId, @PathVariable String rollNo, @RequestParam String className, @RequestParam String section) {
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(2);
        int currentYear = calendar.get(1);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.clear();
        startCalendar.set(currentYear, currentMonth, 1, 0, 0, 0);
        startCalendar.set(14, 0);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.clear();
        endCalendar.set(currentYear, currentMonth, endCalendar.getActualMaximum(5), 23, 59, 59);
        endCalendar.set(14, 999);
        Date endDate = endCalendar.getTime();
        Map attendanceStats = this.attendanceService.calculateStudentAttendance(schoolId, rollNo, className, section, startDate, endDate);
        return ResponseEntity.ok(attendanceStats);
    }

    @GetMapping(value={"/attendance/student/{schoolId}/{rollNo}/current-year"})
    public ResponseEntity<Map<String, Object>> calculateStudentCurrentYearAttendance(@PathVariable String schoolId, @PathVariable String rollNo, @RequestParam String className, @RequestParam String section) {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(1);
        Map attendanceStats = this.attendanceService.calculateStudentYearlyAttendance(schoolId, rollNo, className, section, currentYear);
        Map monthWiseStats = this.attendanceService.calculateStudentMonthWiseAttendance(schoolId, rollNo, className, section, currentYear);
        attendanceStats.put("monthWiseStats", monthWiseStats);
        return ResponseEntity.ok(attendanceStats);
    }

    @GetMapping(value={"/attendance/student/{schoolId}/{rollNo}/month"})
    public ResponseEntity<Map<String, Object>> calculateStudentMonthAttendance(@PathVariable String schoolId, @PathVariable String rollNo, @RequestParam String className, @RequestParam String section, @RequestParam int month, @RequestParam int year) {
        Map attendanceStats = this.attendanceService.calculateStudentMonthlyAttendance(schoolId, rollNo, className, section, month, year);
        return ResponseEntity.ok(attendanceStats);
    }
}

