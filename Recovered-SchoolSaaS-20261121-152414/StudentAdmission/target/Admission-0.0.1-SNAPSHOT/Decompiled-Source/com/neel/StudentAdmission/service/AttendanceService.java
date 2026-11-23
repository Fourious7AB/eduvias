/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.StudentAdmission.model.Admission
 *  com.neel.StudentAdmission.model.AttendanceDetails
 *  com.neel.StudentAdmission.repo.AdmissionRepository
 *  com.neel.StudentAdmission.repo.AttendanceDetailsRepository
 *  com.neel.StudentAdmission.service.AttendanceService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.stereotype.Service
 */
package com.neel.StudentAdmission.service;

import com.neel.StudentAdmission.model.Admission;
import com.neel.StudentAdmission.model.AttendanceDetails;
import com.neel.StudentAdmission.repo.AdmissionRepository;
import com.neel.StudentAdmission.repo.AttendanceDetailsRepository;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    @Autowired
    private AdmissionRepository admissionRepository;
    @Autowired
    private AttendanceDetailsRepository attendanceDetailsRepository;

    public List<Admission> getStudentsByClassAndSection(String className, String section) {
        return this.admissionRepository.findByClassNameAndSection(className, section);
    }

    public List<AttendanceDetails> getAttendanceByClassAndSection(String className, String section) {
        return this.attendanceDetailsRepository.findByClassNameAndSection(className, section);
    }

    public List<AttendanceDetails> getAttendanceByClassSectionAndDate(String className, String section, Date attendanceDate) {
        return this.attendanceDetailsRepository.findByClassNameAndSectionAndAttendanceDate(className, section, attendanceDate);
    }

    public String saveAttendance(String className, String section, Date attendanceDate, Map<String, String> attendance) {
        AttendanceDetails attendanceDetails = new AttendanceDetails();
        attendanceDetails.setClassName(className);
        attendanceDetails.setSection(section);
        attendanceDetails.setAttendanceDate(attendanceDate);
        attendanceDetails.setAttendance(attendance);
        attendanceDetails.setSchoolId("67ca9167d56ec91fc9539234");
        this.attendanceDetailsRepository.save((Object)attendanceDetails);
        return "Attendance saved successfully.";
    }

    public List<Admission> getStudentsByClassSectionAndSchool(String className, String section, String schoolId) {
        return this.admissionRepository.findByClassNameAndSectionAndSchoolId(className, section, schoolId);
    }

    public List<AttendanceDetails> getAttendanceByClassSectionAndSchool(String className, String section, String schoolId) {
        return this.attendanceDetailsRepository.findBySchoolIdAndClassNameAndSection(schoolId, className, section);
    }

    public List<AttendanceDetails> getAttendanceByClassSectionDateAndSchool(String className, String section, Date attendanceDate, String schoolId) {
        return this.attendanceDetailsRepository.findBySchoolIdAndClassNameAndSectionAndAttendanceDate(schoolId, className, section, attendanceDate);
    }

    public String saveAttendanceWithSchool(String className, String section, Date attendanceDate, Map<String, String> attendance, String schoolId) {
        AttendanceDetails attendanceDetails = new AttendanceDetails();
        attendanceDetails.setClassName(className);
        attendanceDetails.setSection(section);
        attendanceDetails.setAttendanceDate(attendanceDate);
        attendanceDetails.setAttendance(attendance);
        attendanceDetails.setSchoolId(schoolId);
        this.attendanceDetailsRepository.save((Object)attendanceDetails);
        return "Attendance saved successfully.";
    }

    public Map<String, Map<String, String>> getStudentAttendanceByClassAndSection(String className, String section, Date startDate, Date endDate) {
        List attendanceRecords = this.attendanceDetailsRepository.findByClassNameAndSectionAndAttendanceDateBetween(className, section, startDate, endDate);
        return this.processAttendanceRecords(attendanceRecords, startDate, endDate);
    }

    public Map<String, Map<String, String>> getStudentAttendanceByClassSectionAndSchool(String className, String section, Date startDate, Date endDate, String schoolId) {
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndClassNameAndSectionAndAttendanceDateBetween(schoolId, className, section, startDate, endDate);
        return this.processAttendanceRecords(attendanceRecords, startDate, endDate);
    }

    private Map<String, Map<String, String>> processAttendanceRecords(List<AttendanceDetails> attendanceRecords, Date startDate, Date endDate) {
        String attendanceValue;
        HashMap<String, Map> studentAttendanceMap = new HashMap<String, Map>();
        List allDates = this.getDatesInRange(startDate, endDate);
        Collections.sort(allDates);
        for (AttendanceDetails record : attendanceRecords) {
            Date dateKey = record.getAttendanceDate();
            if (dateKey.before(startDate) || dateKey.after(endDate)) continue;
            for (String rollNo : record.getAttendance().keySet()) {
                studentAttendanceMap.computeIfAbsent(rollNo, k -> new TreeMap());
                attendanceValue = (String)record.getAttendance().get(rollNo);
                ((Map)studentAttendanceMap.get(rollNo)).put(dateKey, attendanceValue);
            }
        }
        for (String rollNo : studentAttendanceMap.keySet()) {
            for (Date date : allDates) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                attendanceValue = calendar.get(7) == 1 ? "S" : "-";
                ((Map)studentAttendanceMap.get(rollNo)).putIfAbsent(date, attendanceValue);
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Map<String, String>> finalResponse = new HashMap<String, Map<String, String>>();
        for (Map.Entry entry : studentAttendanceMap.entrySet()) {
            LinkedHashMap<String, String> sortedAttendanceData = new LinkedHashMap<String, String>();
            for (Map.Entry dateEntry : ((Map)entry.getValue()).entrySet()) {
                sortedAttendanceData.put(dateFormat.format((Date)dateEntry.getKey()), (String)dateEntry.getValue());
            }
            finalResponse.put((String)entry.getKey(), sortedAttendanceData);
        }
        return finalResponse;
    }

    public List<Date> getDatesInRange(Date startDate, Date endDate) {
        ArrayList<Date> datesInRange = new ArrayList<Date>();
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        while (!startCalendar.after(endCalendar)) {
            datesInRange.add(startCalendar.getTime());
            startCalendar.add(5, 1);
        }
        return datesInRange;
    }

    public List<Map<String, Object>> getFormattedAttendanceByClassAndSection(String className, String section, String monthYear) {
        String[] monthYearParts = monthYear.split("/");
        int month = Integer.parseInt(monthYearParts[0]);
        int year = Integer.parseInt(monthYearParts[1]);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        Map attendanceData = this.getStudentAttendanceByClassAndSection(className, section, Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return this.formatAttendanceResponse(attendanceData);
    }

    public List<Map<String, Object>> getFormattedAttendanceByClassSectionAndSchool(String className, String section, String monthYear, String schoolId) {
        String[] monthYearParts = monthYear.split("/");
        int month = Integer.parseInt(monthYearParts[0]);
        int year = Integer.parseInt(monthYearParts[1]);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        Map attendanceData = this.getStudentAttendanceByClassSectionAndSchool(className, section, Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), schoolId);
        return this.formatAttendanceResponse(attendanceData);
    }

    private List<Map<String, Object>> formatAttendanceResponse(Map<String, Map<String, String>> attendanceData) {
        ArrayList<Map<String, Object>> formattedResponse = new ArrayList<Map<String, Object>>();
        attendanceData.forEach((rollNo, attendanceMap) -> {
            HashMap<String, Object> studentAttendance = new HashMap<String, Object>();
            studentAttendance.put("rollNo", rollNo);
            studentAttendance.put("attendance", attendanceMap);
            formattedResponse.add(studentAttendance);
        });
        return formattedResponse;
    }

    public Map<String, Map<String, String>> getRollNoAndAttendanceByClassAndSection(String className, String section, Date startDate, Date endDate) {
        List attendanceRecords = this.attendanceDetailsRepository.findByClassNameAndSectionAndAttendanceDateBetween(className, section, startDate, endDate);
        return this.processRollNoAndAttendance(attendanceRecords, startDate, endDate);
    }

    public Map<String, Map<String, String>> getRollNoAndAttendanceByClassSectionAndSchool(String className, String section, Date startDate, Date endDate, String schoolId) {
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndClassNameAndSectionAndAttendanceDateBetween(schoolId, className, section, startDate, endDate);
        return this.processRollNoAndAttendance(attendanceRecords, startDate, endDate);
    }

    private Map<String, Map<String, String>> processRollNoAndAttendance(List<AttendanceDetails> attendanceRecords, Date startDate, Date endDate) {
        String attendanceValue;
        HashMap<String, Map> studentAttendanceMap = new HashMap<String, Map>();
        List allDates = this.getDatesInRange(startDate, endDate);
        for (AttendanceDetails record : attendanceRecords) {
            Date dateKey = record.getAttendanceDate();
            if (dateKey.before(startDate) || dateKey.after(endDate)) continue;
            for (String rollNo : record.getAttendance().keySet()) {
                studentAttendanceMap.computeIfAbsent(rollNo, k -> new HashMap());
                attendanceValue = (String)record.getAttendance().get(rollNo);
                ((Map)studentAttendanceMap.get(rollNo)).put(dateKey, attendanceValue);
            }
        }
        for (String rollNo : studentAttendanceMap.keySet()) {
            for (Date date : allDates) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                attendanceValue = calendar.get(7) == 1 ? "S" : "-";
                ((Map)studentAttendanceMap.get(rollNo)).putIfAbsent(date, attendanceValue);
            }
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Map<String, String>> responseMap = new HashMap<String, Map<String, String>>();
        for (Map.Entry entry : studentAttendanceMap.entrySet()) {
            LinkedHashMap<String, String> attendanceMap = new LinkedHashMap<String, String>();
            for (Map.Entry attendanceEntry : ((Map)entry.getValue()).entrySet()) {
                attendanceMap.put(dateFormat.format((Date)attendanceEntry.getKey()), (String)attendanceEntry.getValue());
            }
            responseMap.put((String)entry.getKey(), attendanceMap);
        }
        return responseMap;
    }
}

