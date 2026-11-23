package com.result.Result.service;

import com.result.Result.model.MarksRegister;
import com.result.Result.model.ResponseMessage;
import com.result.Result.repo.MarksRegisterRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
public class MarksRegisterService {
    @Autowired
    private MarksRegisterRepository repository;
    @Value(value="${fetch.student.details}")
    private String fetchStudentDetailsUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    private Double parseOptionalDouble(String value, String fieldLabel) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            double parsed = Double.parseDouble(value.trim());
            if (parsed < 0.0) {
                throw new IllegalArgumentException(fieldLabel + " cannot be negative.");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric value for " + fieldLabel + ".");
        }
    }

    private String validateAndNormalize(MarksRegister marksRegister) {
        if (marksRegister == null) {
            return "Marks Register payload cannot be null.";
        }
        if (marksRegister.getClassName() == null || marksRegister.getClassName().trim().isEmpty()) {
            return "Class name is required.";
        }
        if (marksRegister.getSection() == null || marksRegister.getSection().trim().isEmpty()) {
            return "Section is required.";
        }
        if (marksRegister.getSubject() == null || marksRegister.getSubject().trim().isEmpty()) {
            return "Subject is required.";
        }
        if (marksRegister.getExamType() == null || marksRegister.getExamType().trim().isEmpty()) {
            return "Exam type is required.";
        }
        if (marksRegister.getMarks() == null || marksRegister.getMarks().isEmpty()) {
            return "At least one student mark entry is required.";
        }

        marksRegister.setClassName(marksRegister.getClassName().trim());
        marksRegister.setSection(marksRegister.getSection().trim());
        marksRegister.setSubject(marksRegister.getSubject().trim());
        marksRegister.setExamType(marksRegister.getExamType().trim());
        if (marksRegister.getTeacherName() != null) {
            marksRegister.setTeacherName(marksRegister.getTeacherName().trim());
        }

        Double subjectFull;
        Double projectFull;
        try {
            subjectFull = parseOptionalDouble(marksRegister.getSubjectFullMark(), "subject full mark");
            projectFull = parseOptionalDouble(marksRegister.getProjectFullMark(), "project full mark");
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        if (marksRegister.getFullMarks() < 0.0) {
            return "Full marks cannot be negative.";
        }
        if (marksRegister.getFullMarks() <= 0.0 && (subjectFull != null || projectFull != null)) {
            double computedFull = (subjectFull != null ? subjectFull : 0.0) + (projectFull != null ? projectFull : 0.0);
            marksRegister.setFullMarks(computedFull);
        }

        for (MarksRegister.StudentMark mark : marksRegister.getMarks()) {
            if (mark == null) {
                return "Student mark entry cannot be null.";
            }
            if (mark.getProjectMark() == null) {
                mark.setProjectMark(0.0);
            }
            if (mark.getMark() < 0.0) {
                return "Written mark cannot be negative for roll " + mark.getRoll() + ".";
            }
            if (mark.getProjectMark() < 0.0) {
                return "Project mark cannot be negative for roll " + mark.getRoll() + ".";
            }
            if (subjectFull != null && mark.getMark() > subjectFull) {
                return "Written mark (" + mark.getMark() + ") exceeds subject full mark (" + subjectFull + ") for roll " + mark.getRoll() + ".";
            }
            if (projectFull != null && mark.getProjectMark() > projectFull) {
                return "Project mark (" + mark.getProjectMark() + ") exceeds project full mark (" + projectFull + ") for roll " + mark.getRoll() + ".";
            }
        }
        return null;
    }

    private Optional<MarksRegister> findDuplicate(String className, String section, String subject, String examType, String schoolId, String excludeId) {
        List<MarksRegister> registers = this.repository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(className, section, examType, schoolId);
        if (registers == null) {
            return Optional.empty();
        }
        String normalizedSubject = subject.trim();
        return registers.stream()
                .filter(r -> excludeId == null || !excludeId.equals(r.getId()))
                .filter(r -> r.getSubject() != null && r.getSubject().trim().equalsIgnoreCase(normalizedSubject))
                .findFirst();
    }

    public ResponseEntity<ResponseMessage> createMarksRegister(MarksRegister marksRegister) {
        return this.createMarksRegisterWithSchool(marksRegister, "67ca9167d56ec91fc9539234");
    }

    public ResponseEntity<ResponseMessage> getMarksRegister(String id) {
        return this.repository.findById(id)
                .map(register -> new ResponseEntity<>(new ResponseMessage("Marks Register retrieved successfully.", register), (HttpStatusCode) HttpStatus.OK))
                .orElse(new ResponseEntity<>(new ResponseMessage("Marks Register not found.", null), (HttpStatusCode) HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> updateMarksRegister(String id, MarksRegister marksRegister) {
        return this.updateMarksRegisterWithSchool(id, marksRegister, "67ca9167d56ec91fc9539234");
    }

    public ResponseEntity<ResponseMessage> deleteMarksRegister(String id) {
        if (this.repository.existsById(id)) {
            this.repository.deleteById(id);
            return new ResponseEntity<>(new ResponseMessage("Marks Register deleted successfully.", null), (HttpStatusCode) HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("Marks Register not found.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseMessage> getAllMarksRegisters() {
        String schoolId = "67ca9167d56ec91fc9539234";
        List<MarksRegister> registers = this.repository.findBySchoolId(schoolId);
        return new ResponseEntity<>(new ResponseMessage("Marks Registers retrieved successfully.", registers), (HttpStatusCode) HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> searchMarks(String className, String section, int roll, String examType) {
        String schoolId = "67ca9167d56ec91fc9539234";
        List<MarksRegister> registers = this.repository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(
                        className.trim(), section.trim(), examType.trim(), schoolId);
        if (registers == null || registers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No records found for the given criteria.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        List<Map<String, Object>> results = new ArrayList<>();
        for (MarksRegister register : registers) {
            if (register.getMarks() == null) continue;
            register.getMarks().stream().filter(mark -> mark.getRoll() == roll).forEach(mark -> {
                Map<String, Object> studentData = new HashMap<>();
                studentData.put("registerId", register.getId());
                studentData.put("studentName", mark.getName());
                studentData.put("subject", register.getSubject() != null ? register.getSubject().trim() : "");
                studentData.put("marks", mark.getMark());
                studentData.put("projectMark", mark.getProjectMark() != null ? mark.getProjectMark() : 0.0);
                studentData.put("Full Marks", register.getFullMarks());
                studentData.put("fullMarks", register.getFullMarks());
                results.add(studentData);
            });
        }
        if (results.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No marks found for the given roll number.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ResponseMessage("Records retrieved successfully.", results), (HttpStatusCode) HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> searchMarksSubject(String className, String section, String subject, String examType) {
        return this.searchMarksSubjectWithSchool(className, section, subject, examType, "67ca9167d56ec91fc9539234");
    }

    public ResponseEntity<ResponseMessage> createMarksRegisterWithSchool(MarksRegister marksRegister, String schoolId) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("School ID is required.", null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        String validationError = this.validateAndNormalize(marksRegister);
        if (validationError != null) {
            return new ResponseEntity<>(new ResponseMessage(validationError, null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        marksRegister.setSchoolId(schoolId.trim());
        if (this.findDuplicate(marksRegister.getClassName(), marksRegister.getSection(), marksRegister.getSubject(), marksRegister.getExamType(), marksRegister.getSchoolId(), null).isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Error: A Marks Register for the same class, section, subject, and exam type already exists for this school.", null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        MarksRegister savedRegister = this.repository.save(marksRegister);
        return new ResponseEntity<>(new ResponseMessage("Marks Register created successfully for school.", savedRegister), (HttpStatusCode) HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseMessage> getMarksRegisterWithSchool(String id, String schoolId) {
        return this.repository.findByIdAndSchoolId(id, schoolId)
                .map(register -> new ResponseEntity<>(new ResponseMessage("Marks Register retrieved successfully for school.", register), (HttpStatusCode) HttpStatus.OK))
                .orElse(new ResponseEntity<>(new ResponseMessage("Marks Register not found or does not belong to this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> updateMarksRegisterWithSchool(String id, MarksRegister marksRegister, String schoolId) {
        if (marksRegister == null) {
            return new ResponseEntity<>(new ResponseMessage("Marks Register payload cannot be null.", null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        Optional<MarksRegister> existingOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (existingOpt.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("Marks Register not found or does not belong to this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }

        MarksRegister existingRegister = existingOpt.get();
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
        if (marksRegister.getSubjectFullMark() != null) {
            existingRegister.setSubjectFullMark(marksRegister.getSubjectFullMark());
        }
        if (marksRegister.getProjectFullMark() != null) {
            existingRegister.setProjectFullMark(marksRegister.getProjectFullMark());
        }
        if (marksRegister.getFullMarks() > 0.0) {
            existingRegister.setFullMarks(marksRegister.getFullMarks());
        } else if (marksRegister.getSubjectFullMark() != null || marksRegister.getProjectFullMark() != null) {
            existingRegister.setFullMarks(0.0); // allow validateAndNormalize to recompute from subjectFullMark + projectFullMark
        }
        if (marksRegister.getTeacherName() != null) {
            existingRegister.setTeacherName(marksRegister.getTeacherName());
        }
        if (marksRegister.getMarks() != null) {
            existingRegister.setMarks(marksRegister.getMarks());
        }
        existingRegister.setSchoolId(schoolId.trim());

        String validationError = this.validateAndNormalize(existingRegister);
        if (validationError != null) {
            return new ResponseEntity<>(new ResponseMessage(validationError, null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }

        if (this.findDuplicate(existingRegister.getClassName(), existingRegister.getSection(), existingRegister.getSubject(), existingRegister.getExamType(), existingRegister.getSchoolId(), id).isPresent()) {
            return new ResponseEntity<>(new ResponseMessage("Error: A Marks Register for the same class, section, subject, and exam type already exists for this school.", null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }

        this.repository.save(existingRegister);
        return new ResponseEntity<>(new ResponseMessage("Marks Register updated successfully for school.", existingRegister), (HttpStatusCode) HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> deleteMarksRegisterWithSchool(String id, String schoolId) {
        Optional<MarksRegister> registerOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (registerOpt.isPresent()) {
            this.repository.deleteById(id);
            return new ResponseEntity<>(new ResponseMessage("Marks Register deleted successfully for school.", null), (HttpStatusCode) HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("Marks Register not found or does not belong to this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseMessage> getAllMarksRegistersBySchool(String schoolId) {
        List<MarksRegister> registers = this.repository.findBySchoolId(schoolId);
        return new ResponseEntity<>(new ResponseMessage("Marks Registers retrieved successfully for school.", registers), (HttpStatusCode) HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> searchMarksWithSchool(String className, String section, int roll, String examType, String schoolId) {
        List<MarksRegister> registers = this.repository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(
                        className.trim(), section.trim(), examType.trim(), schoolId.trim());
        if (registers == null || registers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No records found for the given criteria in this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        List<Map<String, Object>> results = new ArrayList<>();
        for (MarksRegister register : registers) {
            if (register.getMarks() == null) continue;
            register.getMarks().stream().filter(mark -> mark.getRoll() == roll).forEach(mark -> {
                Map<String, Object> studentData = new HashMap<>();
                studentData.put("registerId", register.getId());
                studentData.put("studentName", mark.getName());
                studentData.put("roll", mark.getRoll());
                studentData.put("subject", register.getSubject() != null ? register.getSubject().trim() : "");
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
                studentData.put("projectMark", mark.getProjectMark() != null ? mark.getProjectMark() : 0.0);
                studentData.put("projectFullMark", register.getProjectFullMark());
                studentData.put("subjectFullMark", register.getSubjectFullMark());
                studentData.put("fullMarks", register.getFullMarks());
                studentData.put("Teacher", register.getTeacherName());
                results.add(studentData);
            });
        }
        if (results.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No marks found for the given roll number in this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        results.sort(Comparator.comparing(m -> String.valueOf(m.getOrDefault("subject", "")).toLowerCase()));
        return new ResponseEntity<>(new ResponseMessage("Records retrieved successfully for school.", results), (HttpStatusCode) HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> searchMarksSubjectWithSchool(String className, String section, String subject, String examType, String schoolId) {
        if (subject == null || subject.trim().isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("Subject is required.", null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        List<MarksRegister> registers = this.repository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(
                        className.trim(), section.trim(), examType.trim(), schoolId.trim());
        if (registers == null || registers.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No records found for the given criteria in this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        String targetSubject = subject.trim();
        List<Map<String, Object>> results = new ArrayList<>();
        for (MarksRegister register : registers) {
            if (register.getMarks() == null || register.getSubject() == null) continue;
            if (!register.getSubject().trim().equalsIgnoreCase(targetSubject)) continue;

            for (MarksRegister.StudentMark mark : register.getMarks()) {
                Map<String, Object> studentData = new HashMap<>();
                studentData.put("registerId", register.getId());
                studentData.put("studentName", mark.getName());
                studentData.put("roll", mark.getRoll());
                studentData.put("subject", register.getSubject().trim());
                studentData.put("class", register.getClassName());
                studentData.put("section", register.getSection());
                studentData.put("marks", mark.getMark());
                studentData.put("projectMark", mark.getProjectMark() != null ? mark.getProjectMark() : 0.0);
                studentData.put("projectFullMark", register.getProjectFullMark());
                studentData.put("subjectFullMark", register.getSubjectFullMark());
                studentData.put("fullMarks", register.getFullMarks());
                studentData.put("Teacher", register.getTeacherName());
                results.add(studentData);
            }
        }
        if (results.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("No marks found for the given subject in this school.", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        results.sort(Comparator.comparingInt(m -> ((Number) m.getOrDefault("roll", 0)).intValue()));
        return new ResponseEntity<>(new ResponseMessage("Records retrieved successfully for school.", results), (HttpStatusCode) HttpStatus.OK);
    }
}
