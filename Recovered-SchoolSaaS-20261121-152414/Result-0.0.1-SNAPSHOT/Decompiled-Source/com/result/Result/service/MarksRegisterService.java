/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.MarksRegister
 *  com.result.Result.model.ResponseMessage
 *  com.result.Result.repo.MarksRegisterRepository
 *  com.result.Result.service.MarksRegisterService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 *  org.springframework.web.client.RestTemplate
 */
package com.result.Result.service;

import com.result.Result.model.MarksRegister;
import com.result.Result.model.ResponseMessage;
import com.result.Result.repo.MarksRegisterRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarksRegisterService {
    @Autowired
    private MarksRegisterRepository repository;
    @Value(value="${fetch.student.details}")
    private String fetchStudentDetailsUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<ResponseMessage> createMarksRegister(MarksRegister marksRegister) {
        marksRegister.setSchoolId("67ca9167d56ec91fc9539234");
        if (this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSubjectIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(marksRegister.getClassName(), marksRegister.getSection(), marksRegister.getSubject(), marksRegister.getExamType(), marksRegister.getSchoolId()).isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Error: A Marks Register for the same class, section, and subject already exists.", null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        MarksRegister savedRegister = (MarksRegister)this.repository.save((Object)marksRegister);
        return new ResponseEntity((Object)new ResponseMessage("Marks Register created successfully.", (Object)savedRegister), (HttpStatusCode)HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseMessage> getMarksRegister(String id) {
        return this.repository.findById((Object)id).map(register -> new ResponseEntity((Object)new ResponseMessage("Marks Register retrieved successfully.", register), (HttpStatusCode)HttpStatus.OK)).orElse(new ResponseEntity((Object)new ResponseMessage("Marks Register not found.", null), (HttpStatusCode)HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> updateMarksRegister(String id, MarksRegister marksRegister) {
        marksRegister.setSchoolId("67ca9167d56ec91fc9539234");
        return this.repository.findById((Object)id).map(existingRegister -> {
            if (!existingRegister.getId().equals(id) && this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSubjectIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(marksRegister.getClassName(), marksRegister.getSection(), marksRegister.getSubject(), marksRegister.getExamType(), marksRegister.getSchoolId()).isPresent()) {
                return new ResponseEntity((Object)new ResponseMessage("Error: A Marks Register for the same class, section, and subject already exists.", null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
            }
            if (marksRegister.getClassName() != null) {
                existingRegister.setClassName(marksRegister.getClassName());
            }
            if (marksRegister.getSection() != null) {
                existingRegister.setSection(marksRegister.getSection());
            }
            if (marksRegister.getSubject() != null) {
                existingRegister.setSubject(marksRegister.getSubject());
            }
            if (marksRegister.getExamType() != null) {
                existingRegister.setExamType(marksRegister.getExamType());
            }
            existingRegister.setFullMarks(marksRegister.getFullMarks());
            if (marksRegister.getTeacherName() != null) {
                existingRegister.setTeacherName(marksRegister.getTeacherName());
            }
            if (marksRegister.getMarks() != null) {
                existingRegister.setMarks(marksRegister.getMarks());
            }
            if (marksRegister.getSchoolId() != null) {
                existingRegister.setSchoolId(marksRegister.getSchoolId());
            }
            this.repository.save(existingRegister);
            return new ResponseEntity((Object)new ResponseMessage("Marks Register updated successfully.", existingRegister), (HttpStatusCode)HttpStatus.OK);
        }).orElse(new ResponseEntity((Object)new ResponseMessage("Marks Register not found.", null), (HttpStatusCode)HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> deleteMarksRegister(String id) {
        if (this.repository.existsById((Object)id)) {
            this.repository.deleteById((Object)id);
            return new ResponseEntity((Object)new ResponseMessage("Marks Register deleted successfully.", null), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Marks Register not found.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseMessage> getAllMarksRegisters() {
        String schoolId = "67ca9167d56ec91fc9539234";
        List registers = this.repository.findBySchoolId(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Marks Registers retrieved successfully.", (Object)registers), (HttpStatusCode)HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> searchMarks(String className, String section, int roll, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        List registers = this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(className, section, examType, schoolId);
        if (registers.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No records found for the given criteria.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        ArrayList results = new ArrayList();
        for (MarksRegister register : registers) {
            register.getMarks().stream().filter(mark -> mark.getRoll() == roll).forEach(mark -> {
                HashMap<String, Object> studentData = new HashMap<String, Object>();
                studentData.put("studentName", mark.getName());
                studentData.put("subject", register.getSubject());
                studentData.put("marks", mark.getMark());
                studentData.put("Full Marks", register.getFullMarks());
                results.add(studentData);
            });
        }
        if (results.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No marks found for the given roll number.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseMessage("Records retrieved successfully.", results), (HttpStatusCode)HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> searchMarksSubject(String className, String section, String subject, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        List registers = this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(className, section, examType, schoolId);
        if (registers.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No records found for the given criteria.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        ArrayList results = new ArrayList();
        for (MarksRegister register : registers) {
            register.getMarks().stream().filter(mark -> register.getSubject().equals(subject)).forEach(mark -> {
                HashMap<String, Object> studentData = new HashMap<String, Object>();
                studentData.put("studentName", mark.getName());
                studentData.put("admissionNo", register.getAdmissionNo());
                studentData.put("session", register.getSession());
                studentData.put("motherName", register.getMotherName());
                studentData.put("fatherName", register.getFatherName());
                studentData.put("address", register.getAddress());
                studentData.put("phone", register.getContactNo());
                studentData.put("dob", register.getDob());
                studentData.put("email", register.getEmail());
                studentData.put("roll", mark.getRoll());
                studentData.put("subject", register.getSubject());
                studentData.put("class", register.getClassName());
                studentData.put("section", register.getSection());
                studentData.put("marks", mark.getMark());
                studentData.put("projectMark", mark.getProjectMark());
                studentData.put("projectFullMark", register.getProjectFullMark());
                studentData.put("subjectFullMark", register.getSubjectFullMark());
                studentData.put("fullMarks", register.getFullMarks());
                studentData.put("Teacher", register.getTeacherName());
                results.add(studentData);
            });
        }
        if (results.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No marks found for the given subject.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseMessage("Records retrieved successfully.", results), (HttpStatusCode)HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> createMarksRegisterWithSchool(MarksRegister marksRegister, String schoolId) {
        marksRegister.setSchoolId(schoolId);
        if (this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSubjectIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(marksRegister.getClassName(), marksRegister.getSection(), marksRegister.getSubject(), marksRegister.getExamType(), schoolId).isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Error: A Marks Register for the same class, section, and subject already exists for this school.", null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        MarksRegister savedRegister = (MarksRegister)this.repository.save((Object)marksRegister);
        return new ResponseEntity((Object)new ResponseMessage("Marks Register created successfully for school.", (Object)savedRegister), (HttpStatusCode)HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseMessage> getMarksRegisterWithSchool(String id, String schoolId) {
        return this.repository.findByIdAndSchoolId(id, schoolId).map(register -> new ResponseEntity((Object)new ResponseMessage("Marks Register retrieved successfully for school.", register), (HttpStatusCode)HttpStatus.OK)).orElse(new ResponseEntity((Object)new ResponseMessage("Marks Register not found or does not belong to this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> updateMarksRegisterWithSchool(String id, MarksRegister marksRegister, String schoolId) {
        marksRegister.setSchoolId(schoolId);
        return this.repository.findByIdAndSchoolId(id, schoolId).map(existingRegister -> {
            Optional duplicateCheck = this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndSubjectIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(marksRegister.getClassName(), marksRegister.getSection(), marksRegister.getSubject(), marksRegister.getExamType(), schoolId);
            if (duplicateCheck.isPresent() && !((MarksRegister)duplicateCheck.get()).getId().equals(id)) {
                return new ResponseEntity((Object)new ResponseMessage("Error: A Marks Register for the same class, section, and subject already exists for this school.", null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
            }
            existingRegister.setClassName(marksRegister.getClassName());
            existingRegister.setSection(marksRegister.getSection());
            existingRegister.setSubject(marksRegister.getSubject());
            existingRegister.setExamType(marksRegister.getExamType());
            existingRegister.setFullMarks(marksRegister.getFullMarks());
            existingRegister.setTeacherName(marksRegister.getTeacherName());
            existingRegister.setMarks(marksRegister.getMarks());
            existingRegister.setSchoolId(schoolId);
            this.repository.save(existingRegister);
            return new ResponseEntity((Object)new ResponseMessage("Marks Register updated successfully for school.", existingRegister), (HttpStatusCode)HttpStatus.OK);
        }).orElse(new ResponseEntity((Object)new ResponseMessage("Marks Register not found or does not belong to this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> deleteMarksRegisterWithSchool(String id, String schoolId) {
        Optional registerOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (registerOpt.isPresent()) {
            this.repository.deleteById((Object)id);
            return new ResponseEntity((Object)new ResponseMessage("Marks Register deleted successfully for school.", null), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Marks Register not found or does not belong to this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseMessage> getAllMarksRegistersBySchool(String schoolId) {
        List registers = this.repository.findBySchoolId(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Marks Registers retrieved successfully for school.", (Object)registers), (HttpStatusCode)HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> searchMarksWithSchool(String className, String section, int roll, String examType, String schoolId) {
        List registers = this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(className, section, examType, schoolId);
        if (registers.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No records found for the given criteria in this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        ArrayList results = new ArrayList();
        for (MarksRegister register : registers) {
            register.getMarks().stream().filter(mark -> mark.getRoll() == roll).forEach(mark -> {
                HashMap<String, Object> studentData = new HashMap<String, Object>();
                studentData.put("studentName", mark.getName());
                studentData.put("roll", mark.getRoll());
                studentData.put("subject", register.getSubject());
                studentData.put("class", register.getClassName());
                studentData.put("section", register.getSection());
                studentData.put("admissionNo", register.getAdmissionNo());
                studentData.put("session", register.getSession());
                studentData.put("motherName", register.getMotherName());
                studentData.put("fatherName", register.getFatherName());
                studentData.put("address", register.getAddress());
                studentData.put("phone", register.getContactNo());
                studentData.put("dob", register.getDob());
                studentData.put("email", register.getEmail());
                studentData.put("marks", mark.getMark());
                studentData.put("projectMark", mark.getProjectMark());
                studentData.put("projectFullMark", register.getProjectFullMark());
                studentData.put("subjectFullMark", register.getSubjectFullMark());
                studentData.put("fullMarks", register.getFullMarks());
                studentData.put("Teacher", register.getTeacherName());
                results.add(studentData);
            });
        }
        if (results.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No marks found for the given roll number in this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseMessage("Records retrieved successfully for school.", results), (HttpStatusCode)HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> searchMarksSubjectWithSchool(String className, String section, String subject, String examType, String schoolId) {
        List registers = this.repository.findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(className, section, examType, schoolId);
        if (registers.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No records found for the given criteria in this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        ArrayList results = new ArrayList();
        for (MarksRegister register : registers) {
            register.getMarks().stream().filter(mark -> register.getSubject().equals(subject)).forEach(mark -> {
                HashMap<String, Object> studentData = new HashMap<String, Object>();
                studentData.put("studentName", mark.getName());
                studentData.put("roll", mark.getRoll());
                studentData.put("subject", register.getSubject());
                studentData.put("class", register.getClassName());
                studentData.put("section", register.getSection());
                studentData.put("marks", mark.getMark());
                studentData.put("projectMark", mark.getProjectMark());
                studentData.put("projectFullMark", register.getProjectFullMark());
                studentData.put("subjectFullMark", register.getSubjectFullMark());
                studentData.put("fullMarks", register.getFullMarks());
                studentData.put("Teacher", register.getTeacherName());
                results.add(studentData);
            });
        }
        if (results.isEmpty()) {
            return new ResponseEntity((Object)new ResponseMessage("No marks found for the given subject in this school.", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity((Object)new ResponseMessage("Records retrieved successfully for school.", results), (HttpStatusCode)HttpStatus.OK);
    }
}

