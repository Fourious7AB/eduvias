/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.Dto.StudentDetailsResponse
 *  com.result.Result.Dto.StudentDetailsResponse$StudentData
 *  com.result.Result.model.GradeSystem
 *  com.result.Result.model.MarksRegister
 *  com.result.Result.model.MarksRegister$StudentMark
 *  com.result.Result.model.ResponseMessage
 *  com.result.Result.repo.GradeSystemRepository
 *  com.result.Result.repo.MarksRegisterRepository
 *  com.result.Result.service.GradeSystemService
 *  com.result.Result.service.MarksheetService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestTemplate
 */
package com.result.Result.service;

import com.result.Result.Dto.StudentDetailsResponse;
import com.result.Result.model.GradeSystem;
import com.result.Result.model.MarksRegister;
import com.result.Result.model.ResponseMessage;
import com.result.Result.repo.GradeSystemRepository;
import com.result.Result.repo.MarksRegisterRepository;
import com.result.Result.service.GradeSystemService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarksheetService {
    @Autowired
    private MarksRegisterRepository marksRegisterRepository;
    @Autowired
    private GradeSystemRepository gradeSystemRepository;
    @Value(value="${fetch.student.details}")
    private String fetchStudentDetailsUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private GradeSystemService gradeSystemService;

    public ResponseEntity<ResponseMessage> generateStudentMarksheet(String className, String section, int roll, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.generateMarksheet(className, section, roll, examType, schoolId);
    }

    public ResponseEntity<ResponseMessage> generateStudentMarksheetWithSchool(String className, String section, int roll, String examType, String schoolId) {
        return this.generateMarksheet(className, section, roll, examType, schoolId);
    }

    public ResponseEntity<ResponseMessage> generateClassSectionMarksheets(String className, String section, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.generateClassSectionMarksheets(className, section, examType, schoolId);
    }

    public ResponseEntity<ResponseMessage> generateClassSectionMarksheetsWithSchool(String className, String section, String examType, String schoolId) {
        return this.generateClassSectionMarksheets(className, section, examType, schoolId);
    }

    private ResponseEntity<ResponseMessage> generateClassSectionMarksheets(String className, String section, String examType, String schoolId) {
        List registers = this.marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(className, section, examType, schoolId);
        if (registers.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No records found for the given criteria.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        List grades = this.gradeSystemRepository.findBySchoolId(schoolId);
        if (grades.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No grade system defined for this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        HashSet<Integer> rollNumbers = new HashSet<Integer>();
        for (Object register : registers) {
            for (MarksRegister.StudentMark mark : register.getMarks()) {
                rollNumbers.add(mark.getRoll());
            }
        }
        ArrayList<Map> allMarksheets = new ArrayList<Map>();
        for (Integer roll : rollNumbers) {
            Map marksheetData = this.generateMarksheetData(registers, grades, className, section, roll.intValue(), examType, schoolId);
            if (marksheetData == null) continue;
            allMarksheets.add(marksheetData);
        }
        if (allMarksheets.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No student data found for the given criteria.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        allMarksheets.sort(Comparator.comparing(m -> (Integer)m.get("roll")));
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("className", className);
        response.put("section", section);
        response.put("examType", examType);
        response.put("markSheets", allMarksheets);
        response.put("gradeScale", this.gradeSystemService.getAllGradeBySchool(schoolId));
        return new ResponseEntity((Object)new ResponseMessage("Mark-Sheets generated successfully for all students.", response), (HttpStatusCode)HttpStatus.OK);
    }

    private ResponseEntity<ResponseMessage> generateMarksheet(String className, String section, int roll, String examType, String schoolId) {
        List registers = this.marksRegisterRepository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(className, section, examType, schoolId);
        if (registers.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No records found for the given criteria.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        List grades = this.gradeSystemRepository.findBySchoolId(schoolId);
        if (grades.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No grade system defined for this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        Map marksheetData = this.generateMarksheetData(registers, grades, className, section, roll, examType, schoolId);
        HashMap<String, List<Map>> response = new HashMap<String, List<Map>>();
        response.put("gradeScale", this.gradeSystemService.getAllGradeBySchool(schoolId));
        response.put("markSheets", Collections.singletonList(marksheetData));
        if (marksheetData == null) {
            return new ResponseEntity((Object)new ResponseMessage("Student with roll number " + roll + " not found.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseMessage("Marksheet generated successfully.", response), (HttpStatusCode)HttpStatus.OK);
    }

    private Map<String, Object> generateMarksheetData(List<MarksRegister> registers, List<GradeSystem> grades, String className, String section, int roll, String examType, String schoolId) {
        String studentName = null;
        for (MarksRegister register : registers) {
            Optional<MarksRegister.StudentMark> studentMark = register.getMarks().stream().filter(mark -> mark.getRoll() == roll).findFirst();
            if (!studentMark.isPresent()) continue;
            studentName = studentMark.get().getName();
            break;
        }
        if (studentName == null) {
            return null;
        }
        HashMap<String, Object> marksheetData = new HashMap<String, Object>();
        marksheetData.put("name", studentName);
        marksheetData.put("class", className);
        marksheetData.put("section", section);
        marksheetData.put("roll", roll);
        marksheetData.put("examType", examType);
        StudentDetailsResponse.StudentData studentDetails = this.fetchStudentDetails(String.valueOf(roll), className, section, schoolId);
        marksheetData.put("studentDetails", studentDetails);
        ArrayList subjectResults = new ArrayList();
        int grandTotal = 0;
        int subjectCount = 0;
        for (MarksRegister register : registers) {
            Optional<MarksRegister.StudentMark> studentMark = register.getMarks().stream().filter(mark -> mark.getRoll() == roll).findFirst();
            if (!studentMark.isPresent()) continue;
            HashMap<String, Object> subjectResult = new HashMap<String, Object>();
            subjectResult.put("subject", register.getSubject());
            subjectResult.put("subjectFullMark", register.getSubjectFullMark());
            subjectResult.put("projectFullMark", register.getProjectFullMark());
            int writtenMarks = (int)studentMark.get().getMark();
            subjectResult.put("writtenMarks", writtenMarks);
            double projectMarks = studentMark.get().getProjectMark();
            subjectResult.put("projectMarks", projectMarks);
            double totalMarks = (double)writtenMarks + projectMarks;
            subjectResult.put("totalMarks", totalMarks);
            String grade = this.determineGrade(totalMarks, grades);
            subjectResult.put("grade", grade);
            subjectResults.add(subjectResult);
            grandTotal = (int)((double)grandTotal + totalMarks);
            ++subjectCount;
        }
        marksheetData.put("subjects", subjectResults);
        marksheetData.put("totalMarks", grandTotal);
        String overallGrade = "F";
        if (subjectCount > 0) {
            int averageMarks = grandTotal / subjectCount;
            overallGrade = this.determineGrade((double)averageMarks, grades);
        }
        marksheetData.put("overallGrade", overallGrade);
        boolean allPassed = subjectResults.stream().noneMatch(subject -> subject.get("grade").equals("F"));
        marksheetData.put("result", allPassed ? "P" : "F");
        return marksheetData;
    }

    private String determineGrade(double marks, List<GradeSystem> grades) {
        for (GradeSystem grade : grades) {
            if (!(marks >= (double)grade.getScaleStartMarks()) || !(marks <= (double)grade.getScaleEndMarks())) continue;
            return grade.getLetterGrade();
        }
        return "F";
    }

    public StudentDetailsResponse.StudentData fetchStudentDetails(String rollNo, String className, String section, String schoolId) {
        String url = String.format(this.fetchStudentDetailsUrl + "?rollNo=%s&className=%s&section=%s&schoolId=%s", rollNo, className, section, schoolId);
        StudentDetailsResponse response = (StudentDetailsResponse)this.restTemplate.getForObject(url, StudentDetailsResponse.class, new Object[0]);
        return response != null ? response.getData() : null;
    }
}

