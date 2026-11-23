package com.result.Result.service;

import com.result.Result.model.AdditionalMarksRegister;
import com.result.Result.model.MarksRegister;
import com.result.Result.model.ResponseModel;
import com.result.Result.repo.AdditionalMarksRegisterRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdditionalMarksRegisterService {
    @Autowired
    private AdditionalMarksRegisterRepository repository;

    private String validateAndNormalize(AdditionalMarksRegister payload) {
        if (payload == null) {
            return "Payload cannot be null";
        }
        if (payload.getSchoolId() == null || payload.getSchoolId().trim().isEmpty()) {
            return "School ID is required";
        }
        if (payload.getClassName() == null || payload.getClassName().trim().isEmpty()) {
            return "Class name is required";
        }
        if (payload.getSection() == null || payload.getSection().trim().isEmpty()) {
            return "Section is required";
        }
        if (payload.getExamType() == null || payload.getExamType().trim().isEmpty()) {
            return "Exam type is required";
        }
        if (payload.getSubject() == null || payload.getSubject().trim().isEmpty()) {
            return "Subject name is required";
        }
        if (payload.getSubjectFullMark() < 0.0 || payload.getProjectFullMark() < 0.0) {
            return "Full marks cannot be negative";
        }
        if (payload.getSubjectFullMark() <= 0.0 && payload.getProjectFullMark() <= 0.0) {
            return "Total full marks must be greater than 0";
        }
        if (payload.getMarks() == null || payload.getMarks().isEmpty()) {
            return "At least one student mark is required";
        }

        payload.setSchoolId(payload.getSchoolId().trim());
        payload.setClassName(payload.getClassName().trim());
        payload.setSection(payload.getSection().trim());
        payload.setExamType(payload.getExamType().trim());
        payload.setSubject(payload.getSubject().trim());
        payload.setFullMarks(payload.getSubjectFullMark() + payload.getProjectFullMark());
        if (payload.getTeacherName() != null) {
            payload.setTeacherName(payload.getTeacherName().trim());
        }

        for (MarksRegister.StudentMark m : payload.getMarks()) {
            if (m == null) {
                return "Student mark entry cannot be null";
            }
            if (m.getProjectMark() == null) {
                m.setProjectMark(0.0);
            }
            if (m.getMark() < 0.0 || m.getProjectMark() < 0.0) {
                return "Marks cannot be negative for roll " + m.getRoll();
            }
            if (m.getMark() > payload.getSubjectFullMark()) {
                return "Written mark exceeds subject full mark for roll " + m.getRoll();
            }
            if (m.getProjectMark() > payload.getProjectFullMark()) {
                return "Project mark exceeds project full mark for roll " + m.getRoll();
            }
        }
        return null;
    }

    private Optional<AdditionalMarksRegister> findExisting(String schoolId, String className, String section, String examType, String subject) {
        List<AdditionalMarksRegister> registers = this.repository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(
                        className.trim(), section.trim(), examType.trim(), schoolId.trim());
        if (registers == null) {
            return Optional.empty();
        }
        String targetSub = subject.trim();
        return registers.stream()
                .filter(r -> r.getSubject() != null && r.getSubject().trim().equalsIgnoreCase(targetSub))
                .findFirst();
    }

    public ResponseEntity<ResponseModel> create(AdditionalMarksRegister payload) {
        String error = validateAndNormalize(payload);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, error, null));
        }
        if (findExisting(payload.getSchoolId(), payload.getClassName(), payload.getSection(), payload.getExamType(), payload.getSubject()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, "Additional mark register already exists for this class, section, exam type, and subject", null));
        }
        payload.setId(null);
        AdditionalMarksRegister saved = this.repository.save(payload);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseModel(true, "Additional subject marks saved successfully", saved));
    }

    public ResponseEntity<ResponseModel> getAdditionalMarksheetData(String schoolId, String className, String section, String examType) {
        if (schoolId == null || schoolId.trim().isEmpty()
                || className == null || className.trim().isEmpty()
                || section == null || section.trim().isEmpty()
                || examType == null || examType.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, "schoolId, className, section, and examType are required", null));
        }

        List<AdditionalMarksRegister> registers = this.repository
                .findByClassNameIgnoreCaseAndSectionIgnoreCaseAndExamTypeIgnoreCaseAndSchoolId(
                        className.trim(), section.trim(), examType.trim(), schoolId.trim());

        Map<Integer, Map<String, Object>> studentByRoll = new LinkedHashMap<>();
        if (registers != null) {
            for (AdditionalMarksRegister reg : registers) {
                if (reg.getMarks() == null) continue;
                for (MarksRegister.StudentMark sm : reg.getMarks()) {
                    Map<String, Object> studentEntry = studentByRoll.computeIfAbsent(sm.getRoll(), r -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("roll", sm.getRoll());
                        map.put("name", sm.getName());
                        map.put("subjects", new ArrayList<Map<String, Object>>());
                        return map;
                    });
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> subjects = (List<Map<String, Object>>) studentEntry.get("subjects");

                    double written = sm.getMark();
                    double proj = sm.getProjectMark() != null ? sm.getProjectMark() : 0.0;
                    double total = Math.round((written + proj) * 100.0) / 100.0;

                    Map<String, Object> sub = new HashMap<>();
                    sub.put("subject", reg.getSubject());
                    sub.put("subjectFullMark", reg.getSubjectFullMark());
                    sub.put("projectFullMark", reg.getProjectFullMark());
                    sub.put("fullMarks", reg.getFullMarks() > 0 ? reg.getFullMarks() : (reg.getSubjectFullMark() + reg.getProjectFullMark()));
                    sub.put("writtenMarks", written);
                    sub.put("projectMarks", proj);
                    sub.put("totalMarks", total);
                    sub.put("isAdditional", true);
                    subjects.add(sub);
                }
            }
        }

        List<Map<String, Object>> markSheets = new ArrayList<>(studentByRoll.values());
        markSheets.sort(Comparator.comparingInt(m -> ((Number) m.get("roll")).intValue()));

        Map<String, Object> data = new HashMap<>();
        data.put("className", className.trim());
        data.put("section", section.trim());
        data.put("examType", examType.trim());
        data.put("markSheets", markSheets);

        return ResponseEntity.ok(new ResponseModel(true, "Additional marksheet data fetched successfully", data));
    }

    public ResponseEntity<ResponseModel> update(AdditionalMarksRegister payload) {
        String error = validateAndNormalize(payload);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, error, null));
        }
        Optional<AdditionalMarksRegister> existingOpt = findExisting(
                payload.getSchoolId(), payload.getClassName(), payload.getSection(), payload.getExamType(), payload.getSubject());
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseModel(false, "Additional mark register not found", null));
        }

        AdditionalMarksRegister existing = existingOpt.get();
        existing.setSubjectFullMark(payload.getSubjectFullMark());
        existing.setProjectFullMark(payload.getProjectFullMark());
        existing.setFullMarks(payload.getFullMarks());
        existing.setTeacherName(payload.getTeacherName());
        existing.setUserRole(payload.getUserRole());
        existing.setMarks(payload.getMarks());

        AdditionalMarksRegister saved = this.repository.save(existing);
        return ResponseEntity.ok(new ResponseModel(true, "Additional subject marks updated successfully", saved));
    }

    public ResponseEntity<ResponseModel> delete(String schoolId, String className, String section, String examType, String subject) {
        if (schoolId == null || className == null || section == null || examType == null || subject == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, "schoolId, className, section, examType, and subject are required", null));
        }
        Optional<AdditionalMarksRegister> existingOpt = findExisting(schoolId, className, section, examType, subject);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseModel(false, "Additional mark register not found", null));
        }
        this.repository.deleteById(existingOpt.get().getId());
        return ResponseEntity.ok(new ResponseModel(true, "Additional subject marks deleted successfully", null));
    }
}
