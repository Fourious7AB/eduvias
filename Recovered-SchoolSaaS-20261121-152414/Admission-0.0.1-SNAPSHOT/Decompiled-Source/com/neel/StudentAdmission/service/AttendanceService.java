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
import java.util.Iterator;
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
        return this.admissionRepository.findByClassNameIgnoreCaseAndSectionIgnoreCase(className, section);
    }

    public List<AttendanceDetails> getAttendanceByClassAndSection(String className, String section) {
        return this.attendanceDetailsRepository.findByClassNameIgnoreCaseAndSectionIgnoreCase(className, section);
    }

    public List<AttendanceDetails> getAttendanceByClassSectionAndDate(String className, String section, Date attendanceDate) {
        return this.attendanceDetailsRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndAttendanceDate(className, section, attendanceDate);
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
        return this.admissionRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSchoolId(className, section, schoolId);
    }

    public List<AttendanceDetails> getAttendanceByClassSectionAndSchool(String className, String section, String schoolId) {
        return this.attendanceDetailsRepository.findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCase(schoolId, className, section);
    }

    public List<AttendanceDetails> getAttendanceByClassSectionDateAndSchool(String className, String section, Date attendanceDate, String schoolId) {
        return this.attendanceDetailsRepository.findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCaseAndAttendanceDate(schoolId, className, section, attendanceDate);
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
        List attendanceRecords = this.attendanceDetailsRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndAttendanceDateBetween(className, section, startDate, endDate);
        return this.processAttendanceRecords(attendanceRecords, startDate, endDate);
    }

    public Map<String, Map<String, String>> getStudentAttendanceByClassSectionAndSchool(String className, String section, Date startDate, Date endDate, String schoolId) {
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCaseAndAttendanceDateBetween(schoolId, className, section, startDate, endDate);
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
        List attendanceRecords = this.attendanceDetailsRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndAttendanceDateBetween(className, section, startDate, endDate);
        return this.processRollNoAndAttendance(attendanceRecords, startDate, endDate);
    }

    public Map<String, Map<String, String>> getRollNoAndAttendanceByClassSectionAndSchool(String className, String section, Date startDate, Date endDate, String schoolId) {
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCaseAndAttendanceDateBetween(schoolId, className, section, startDate, endDate);
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

    public Map<String, Object> calculateAttendanceStatsBySchool(String schoolId, Date startDate, Date endDate) {
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndAttendanceDateBetween(schoolId, startDate, endDate);
        int totalPresent = 0;
        int totalAbsent = 0;
        int totalSunday = 0;
        int totalHoliday = 0;
        int totalLeave = 0;
        HashMap<String, Map> classWiseStats = new HashMap<String, Map>();
        for (AttendanceDetails record : attendanceRecords) {
            String className = record.getClassName();
            String section = record.getSection();
            String classKey = className + "-" + section;
            classWiseStats.computeIfAbsent(classKey, k -> new HashMap());
            Map classStats = (Map)classWiseStats.get(classKey);
            classStats.putIfAbsent("present", 0);
            classStats.putIfAbsent("absent", 0);
            classStats.putIfAbsent("sunday", 0);
            classStats.putIfAbsent("holiday", 0);
            classStats.putIfAbsent("leave", 0);
            Iterator iterator = record.getAttendance().values().iterator();
            while (iterator.hasNext()) {
                String status;
                switch (status = (String)iterator.next()) {
                    case "P": {
                        ++totalPresent;
                        classStats.put("present", (Integer)classStats.get("present") + 1);
                        break;
                    }
                    case "A": {
                        ++totalAbsent;
                        classStats.put("absent", (Integer)classStats.get("absent") + 1);
                        break;
                    }
                    case "S": {
                        ++totalSunday;
                        classStats.put("sunday", (Integer)classStats.get("sunday") + 1);
                        break;
                    }
                    case "H": {
                        ++totalHoliday;
                        classStats.put("holiday", (Integer)classStats.get("holiday") + 1);
                        break;
                    }
                    case "L": {
                        ++totalLeave;
                        classStats.put("leave", (Integer)classStats.get("leave") + 1);
                        break;
                    }
                }
            }
        }
        int totalAttendance = totalPresent + totalAbsent + totalSunday + totalHoliday + totalLeave;
        double attendancePercentage = 0.0;
        int countableAttendance = totalPresent + totalAbsent + totalLeave;
        if (countableAttendance > 0) {
            attendancePercentage = (double)totalPresent / (double)countableAttendance * 100.0;
        }
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("schoolId", schoolId);
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("totalAttendance", totalAttendance);
        response.put("totalPresent", totalPresent);
        response.put("totalAbsent", totalAbsent);
        response.put("totalSunday", totalSunday);
        response.put("totalHoliday", totalHoliday);
        response.put("totalLeave", totalLeave);
        response.put("attendancePercentage", (double)Math.round(attendancePercentage * 100.0) / 100.0);
        response.put("classWiseStats", classWiseStats);
        return response;
    }

    public Map<String, Object> calculateWeeklyAttendance(String schoolId, Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(5, 6);
        Date endDate = calendar.getTime();
        return this.calculateAttendanceStatsBySchool(schoolId, startDate, endDate);
    }

    public Map<String, Object> calculateMonthlyAttendance(String schoolId, int month, int year) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, month - 1, 1, 0, 0, 0);
        startCalendar.set(14, 0);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, month - 1, 1, 23, 59, 59);
        endCalendar.set(14, 999);
        endCalendar.set(5, endCalendar.getActualMaximum(5));
        Date endDate = endCalendar.getTime();
        return this.calculateAttendanceStatsBySchool(schoolId, startDate, endDate);
    }

    public Map<String, Object> calculateYearlyAttendance(String schoolId, int year) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, 0, 1, 0, 0, 0);
        startCalendar.set(14, 0);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, 11, 31, 23, 59, 59);
        endCalendar.set(14, 999);
        Date endDate = endCalendar.getTime();
        return this.calculateAttendanceStatsBySchool(schoolId, startDate, endDate);
    }

    public Map<String, Map<String, Integer>> calculateDateWiseAttendance(String schoolId, Date startDate, Date endDate) {
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndAttendanceDateBetween(schoolId, startDate, endDate);
        LinkedHashMap<String, Map<String, Integer>> dateWiseStats = new LinkedHashMap<String, Map<String, Integer>>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        while (!calendar.getTime().after(endDate)) {
            String dateKey = dateFormat.format(calendar.getTime());
            HashMap<String, Integer> dateStat = new HashMap<String, Integer>();
            dateStat.put("present", 0);
            dateStat.put("absent", 0);
            dateStat.put("leave", 0);
            dateStat.put("holiday", 0);
            dateWiseStats.put(dateKey, dateStat);
            calendar.add(5, 1);
        }
        for (AttendanceDetails record : attendanceRecords) {
            String dateKey = dateFormat.format(record.getAttendanceDate());
            Map dateStats = (Map)dateWiseStats.get(dateKey);
            if (dateStats == null) continue;
            Iterator iterator = record.getAttendance().values().iterator();
            while (iterator.hasNext()) {
                String status;
                switch (status = (String)iterator.next()) {
                    case "P": {
                        dateStats.put("present", (Integer)dateStats.get("present") + 1);
                        break;
                    }
                    case "A": {
                        dateStats.put("absent", (Integer)dateStats.get("absent") + 1);
                        break;
                    }
                    case "L": {
                        dateStats.put("leave", (Integer)dateStats.get("leave") + 1);
                        break;
                    }
                    case "H": {
                        dateStats.put("holiday", (Integer)dateStats.get("holiday") + 1);
                        break;
                    }
                }
            }
        }
        return dateWiseStats;
    }

    public Map<String, Map<String, Integer>> calculateDayWiseAttendance(String schoolId, Date startDate, Date endDate) {
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndAttendanceDateBetween(schoolId, startDate, endDate);
        LinkedHashMap<String, Map<String, Integer>> dayWiseStats = new LinkedHashMap<String, Map<String, Integer>>();
        String[] daysOfWeek = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String day : daysOfWeek) {
            HashMap<String, Integer> dayStat = new HashMap<String, Integer>();
            dayStat.put("present", 0);
            dayStat.put("absent", 0);
            dayStat.put("leave", 0);
            dayStat.put("holiday", 0);
            dayWiseStats.put(day, dayStat);
        }
        for (AttendanceDetails record : attendanceRecords) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(record.getAttendanceDate());
            int dayOfWeek = calendar.get(7);
            String dayName = daysOfWeek[dayOfWeek - 2 >= 0 ? dayOfWeek - 2 : 6];
            Map dayStats = (Map)dayWiseStats.get(dayName);
            Iterator iterator = record.getAttendance().values().iterator();
            while (iterator.hasNext()) {
                String status;
                switch (status = (String)iterator.next()) {
                    case "P": {
                        dayStats.put("present", (Integer)dayStats.get("present") + 1);
                        break;
                    }
                    case "A": {
                        dayStats.put("absent", (Integer)dayStats.get("absent") + 1);
                        break;
                    }
                    case "L": {
                        dayStats.put("leave", (Integer)dayStats.get("leave") + 1);
                        break;
                    }
                    case "H": {
                        dayStats.put("holiday", (Integer)dayStats.get("holiday") + 1);
                        break;
                    }
                }
            }
        }
        return dayWiseStats;
    }

    public Map<String, Map<String, Integer>> calculateMonthWiseAttendance(String schoolId, int year) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, 0, 1, 0, 0, 0);
        startCalendar.set(14, 0);
        Date yearStartDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, 11, 31, 23, 59, 59);
        endCalendar.set(14, 999);
        Date yearEndDate = endCalendar.getTime();
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndAttendanceDateBetween(schoolId, yearStartDate, yearEndDate);
        LinkedHashMap<String, Map<String, Integer>> monthWiseStats = new LinkedHashMap<String, Map<String, Integer>>();
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            HashMap<String, Integer> monthStat = new HashMap<String, Integer>();
            monthStat.put("present", 0);
            monthStat.put("absent", 0);
            monthStat.put("leave", 0);
            monthStat.put("holiday", 0);
            monthStat.put("sunday", 0);
            monthWiseStats.put(month, monthStat);
        }
        for (AttendanceDetails record : attendanceRecords) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(record.getAttendanceDate());
            int monthIndex = calendar.get(2);
            String monthName = months[monthIndex];
            Map monthStats = (Map)monthWiseStats.get(monthName);
            Iterator iterator = record.getAttendance().values().iterator();
            while (iterator.hasNext()) {
                String status;
                switch (status = (String)iterator.next()) {
                    case "P": {
                        monthStats.put("present", (Integer)monthStats.get("present") + 1);
                        break;
                    }
                    case "A": {
                        monthStats.put("absent", (Integer)monthStats.get("absent") + 1);
                        break;
                    }
                    case "L": {
                        monthStats.put("leave", (Integer)monthStats.get("leave") + 1);
                        break;
                    }
                    case "H": {
                        monthStats.put("holiday", (Integer)monthStats.get("holiday") + 1);
                        break;
                    }
                    case "S": {
                        monthStats.put("sunday", (Integer)monthStats.get("sunday") + 1);
                        break;
                    }
                }
            }
        }
        return monthWiseStats;
    }

    public Map<String, Object> calculateStudentAttendance(String schoolId, String rollNo, String className, String section, Date startDate, Date endDate) {
        List attendanceRecords = this.attendanceDetailsRepository.findBySchoolIdAndClassNameIgnoreCaseAndSectionIgnoreCaseAndAttendanceDateBetween(schoolId, className, section, startDate, endDate);
        int totalPresent = 0;
        int totalAbsent = 0;
        int totalSunday = 0;
        int totalHoliday = 0;
        int totalLeave = 0;
        LinkedHashMap<String, String> dateWiseAttendance = new LinkedHashMap<String, String>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List allDates = this.getDatesInRange(startDate, endDate);
        Collections.sort(allDates);
        for (Date date : allDates) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            String defaultValue = calendar.get(7) == 1 ? "S" : "-";
            dateWiseAttendance.put(dateFormat.format(date), defaultValue);
            if (!defaultValue.equals("S")) continue;
            ++totalSunday;
        }
        for (AttendanceDetails record : attendanceRecords) {
            if (!record.getAttendance().containsKey(rollNo)) continue;
            String status = (String)record.getAttendance().get(rollNo);
            String dateKey = dateFormat.format(record.getAttendanceDate());
            if (dateWiseAttendance.containsKey(dateKey) && ((String)dateWiseAttendance.get(dateKey)).equals("S")) {
                --totalSunday;
            }
            dateWiseAttendance.put(dateKey, status);
            switch (status) {
                case "P": {
                    ++totalPresent;
                    break;
                }
                case "A": {
                    ++totalAbsent;
                    break;
                }
                case "S": {
                    ++totalSunday;
                    break;
                }
                case "H": {
                    ++totalHoliday;
                    break;
                }
                case "L": {
                    ++totalLeave;
                    break;
                }
            }
        }
        int totalDaysInRange = allDates.size();
        int totalWorkingDays = totalDaysInRange - totalSunday - totalHoliday;
        double attendancePercentage = 0.0;
        if (totalWorkingDays > 0) {
            attendancePercentage = (double)totalPresent / (double)totalWorkingDays * 100.0;
        }
        int totalAttendanceDays = totalPresent + totalAbsent + totalLeave;
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("schoolId", schoolId);
        response.put("rollNo", rollNo);
        response.put("className", className);
        response.put("section", section);
        response.put("startDate", startDate);
        response.put("endDate", endDate);
        response.put("totalPresent", totalPresent);
        response.put("totalAbsent", totalAbsent);
        response.put("totalSunday", totalSunday);
        response.put("totalHoliday", totalHoliday);
        response.put("totalLeave", totalLeave);
        response.put("totalWorkingDays", totalWorkingDays);
        response.put("totalAttendanceDays", totalAttendanceDays);
        response.put("attendancePercentage", (double)Math.round(attendancePercentage * 100.0) / 100.0);
        response.put("dateWiseAttendance", dateWiseAttendance);
        return response;
    }

    public Map<String, Object> calculateStudentWeeklyAttendance(String schoolId, String rollNo, String className, String section, Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(5, 6);
        Date endDate = calendar.getTime();
        return this.calculateStudentAttendance(schoolId, rollNo, className, section, startDate, endDate);
    }

    public Map<String, Object> calculateStudentMonthlyAttendance(String schoolId, String rollNo, String className, String section, int month, int year) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, month - 1, 1, 0, 0, 0);
        startCalendar.set(14, 0);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, month - 1, 1, 23, 59, 59);
        endCalendar.set(14, 999);
        endCalendar.set(5, endCalendar.getActualMaximum(5));
        Date endDate = endCalendar.getTime();
        return this.calculateStudentAttendance(schoolId, rollNo, className, section, startDate, endDate);
    }

    public Map<String, Object> calculateStudentYearlyAttendance(String schoolId, String rollNo, String className, String section, int year) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, 0, 1, 0, 0, 0);
        startCalendar.set(14, 0);
        Date startDate = startCalendar.getTime();
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, 11, 31, 23, 59, 59);
        endCalendar.set(14, 999);
        Date endDate = endCalendar.getTime();
        return this.calculateStudentAttendance(schoolId, rollNo, className, section, startDate, endDate);
    }

    public Map<String, Map<String, Object>> calculateStudentMonthWiseAttendance(String schoolId, String rollNo, String className, String section, int year) {
        LinkedHashMap<String, Map<String, Object>> monthWiseStats = new LinkedHashMap<String, Map<String, Object>>();
        String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int month = 1; month <= 12; ++month) {
            Map monthStats = this.calculateStudentMonthlyAttendance(schoolId, rollNo, className, section, month, year);
            HashMap summarizedStats = new HashMap();
            summarizedStats.put("present", monthStats.get("totalPresent"));
            summarizedStats.put("absent", monthStats.get("totalAbsent"));
            summarizedStats.put("leave", monthStats.get("totalLeave"));
            summarizedStats.put("sunday", monthStats.get("totalSunday"));
            summarizedStats.put("holiday", monthStats.get("totalHoliday"));
            summarizedStats.put("attendancePercentage", monthStats.get("attendancePercentage"));
            monthWiseStats.put(months[month - 1], summarizedStats);
        }
        return monthWiseStats;
    }
}

