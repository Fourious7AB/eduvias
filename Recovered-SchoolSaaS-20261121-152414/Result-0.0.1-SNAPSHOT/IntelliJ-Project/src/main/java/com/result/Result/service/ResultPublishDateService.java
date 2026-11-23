package com.result.Result.service;

import com.result.Result.model.ResponseModel;
import com.result.Result.model.ResultPublishDate;
import com.result.Result.repo.ResultPublishDateRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResultPublishDateService {
    @Autowired
    private ResultPublishDateRepository repository;

    private String validate(String schoolId, ResultPublishDate payload) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            return "School ID is required";
        }
        if (payload == null) {
            return "Request body is required";
        }
        if (payload.getClassName() == null || payload.getClassName().trim().isEmpty()) {
            return "Class name is required";
        }
        if (payload.getExamType() == null || payload.getExamType().trim().isEmpty()) {
            return "Exam type is required";
        }
        if (payload.getResultPublishDate() == null || payload.getResultPublishDate().trim().isEmpty()) {
            return "Result publish date is required";
        }
        return null;
    }

    public ResponseEntity<ResponseModel> create(String schoolId, ResultPublishDate payload) {
        String error = validate(schoolId, payload);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, error, null));
        }
        String cleanSchoolId = schoolId.trim();
        String cleanClass = payload.getClassName().trim();
        String cleanExam = payload.getExamType().trim();
        String cleanDate = payload.getResultPublishDate().trim();

        List<ResultPublishDate> existing = this.repository.findBySchoolId(cleanSchoolId);
        boolean duplicate = existing.stream().anyMatch(item ->
                item.getClassName() != null && item.getExamType() != null
                        && item.getClassName().trim().equalsIgnoreCase(cleanClass)
                        && item.getExamType().trim().equalsIgnoreCase(cleanExam));
        if (duplicate) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, "Result publish date already exists for this class and exam type", null));
        }

        payload.setId(null);
        payload.setSchoolId(cleanSchoolId);
        payload.setClassName(cleanClass);
        payload.setExamType(cleanExam);
        payload.setResultPublishDate(cleanDate);

        ResultPublishDate saved = this.repository.save(payload);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseModel(true, "Result publish date created successfully", saved));
    }

    public ResponseEntity<ResponseModel> getAll(String schoolId) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, "School ID is required", null));
        }
        List<ResultPublishDate> list = this.repository.findBySchoolId(schoolId.trim());
        return ResponseEntity.ok(new ResponseModel(true, "Result publish dates retrieved successfully", list));
    }

    public ResponseEntity<ResponseModel> search(String schoolId, String className, String examType) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, "School ID is required", null));
        }
        String targetClass = className != null ? className.trim() : "";
        String targetExam = examType != null ? examType.trim() : "";

        List<ResultPublishDate> filtered = this.repository.findBySchoolId(schoolId.trim()).stream()
                .filter(item -> targetClass.isEmpty()
                        || (item.getClassName() != null && item.getClassName().trim().equalsIgnoreCase(targetClass)))
                .filter(item -> targetExam.isEmpty()
                        || (item.getExamType() != null && item.getExamType().trim().equalsIgnoreCase(targetExam)))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseModel(true, "Result publish dates retrieved successfully", filtered));
    }

    public ResponseEntity<ResponseModel> update(String schoolId, String id, ResultPublishDate payload) {
        String error = validate(schoolId, payload);
        if (error != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseModel(false, error, null));
        }
        String cleanSchoolId = schoolId.trim();
        Optional<ResultPublishDate> existingOpt = this.repository.findByIdAndSchoolId(id, cleanSchoolId);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseModel(false, "Result publish date not found", null));
        }

        String cleanClass = payload.getClassName().trim();
        String cleanExam = payload.getExamType().trim();
        String cleanDate = payload.getResultPublishDate().trim();

        boolean duplicate = this.repository.findBySchoolId(cleanSchoolId).stream()
                .filter(item -> item.getId() != null && !item.getId().equals(id))
                .anyMatch(item -> item.getClassName() != null && item.getExamType() != null
                        && item.getClassName().trim().equalsIgnoreCase(cleanClass)
                        && item.getExamType().trim().equalsIgnoreCase(cleanExam));
        if (duplicate) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, "Result publish date already exists for this class and exam type", null));
        }

        ResultPublishDate record = existingOpt.get();
        record.setClassName(cleanClass);
        record.setExamType(cleanExam);
        record.setResultPublishDate(cleanDate);
        ResultPublishDate saved = this.repository.save(record);

        return ResponseEntity.ok(new ResponseModel(true, "Result publish date updated successfully", saved));
    }

    public ResponseEntity<ResponseModel> delete(String schoolId, String id) {
        if (schoolId == null || schoolId.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel(false, "School ID is required", null));
        }
        Optional<ResultPublishDate> existingOpt = this.repository.findByIdAndSchoolId(id, schoolId.trim());
        if (existingOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseModel(false, "Result publish date not found", null));
        }
        this.repository.deleteById(id);
        return ResponseEntity.ok(new ResponseModel(true, "Result publish date deleted successfully", null));
    }
}
