package com.result.Result.service;

import com.result.Result.model.GradeSystem;
import com.result.Result.model.ResponseMessage;
import com.result.Result.repo.GradeSystemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GradeSystemService {
    @Autowired
    private GradeSystemRepository repository;

    private String validateGradeForSchool(GradeSystem gradeSystem, String schoolId, String excludeId) {
        if (gradeSystem == null) {
            return "Grade details cannot be null";
        }
        if (schoolId == null || schoolId.trim().isEmpty()) {
            return "School ID cannot be null or empty";
        }
        if (gradeSystem.getLetterGrade() == null || gradeSystem.getLetterGrade().trim().isEmpty()) {
            return "Letter grade cannot be empty";
        }
        if (gradeSystem.getPerformanceIndicator() == null || gradeSystem.getPerformanceIndicator().trim().isEmpty()) {
            return "Performance Indicator cannot be empty";
        }
        if (gradeSystem.getScaleStartMarks() < 0 || gradeSystem.getScaleEndMarks() < 0
                || gradeSystem.getScaleStartMarks() > 100 || gradeSystem.getScaleEndMarks() > 100) {
            return "Marks scale must be between 0 and 100";
        }
        if (gradeSystem.getScaleStartMarks() > gradeSystem.getScaleEndMarks()) {
            return "Scale start marks cannot be greater than scale end marks";
        }

        String trimmedGrade = gradeSystem.getLetterGrade().trim();
        String trimmedIndicator = gradeSystem.getPerformanceIndicator().trim();
        gradeSystem.setLetterGrade(trimmedGrade);
        gradeSystem.setPerformanceIndicator(trimmedIndicator);

        List<GradeSystem> existingGrades = this.repository.findBySchoolId(schoolId);
        if (existingGrades != null) {
            for (GradeSystem existing : existingGrades) {
                if (excludeId != null && excludeId.equals(existing.getId())) {
                    continue;
                }
                if (existing.getPerformanceIndicator() != null
                        && existing.getPerformanceIndicator().trim().equalsIgnoreCase(trimmedIndicator)) {
                    return "Performance Indicator already exists for this school";
                }
                if (existing.getLetterGrade() != null
                        && existing.getLetterGrade().trim().equalsIgnoreCase(trimmedGrade)) {
                    return "Letter grade '" + trimmedGrade + "' already exists for this school";
                }
                boolean overlaps = gradeSystem.getScaleStartMarks() <= existing.getScaleEndMarks()
                        && gradeSystem.getScaleEndMarks() >= existing.getScaleStartMarks();
                if (overlaps) {
                    return "Marks range (" + gradeSystem.getScaleStartMarks() + "-" + gradeSystem.getScaleEndMarks()
                            + ") overlaps with existing grade " + existing.getLetterGrade()
                            + " (" + existing.getScaleStartMarks() + "-" + existing.getScaleEndMarks() + ")";
                }
            }
        }
        return null;
    }

    public ResponseEntity<ResponseMessage> createGrade(GradeSystem gradeSystem) {
        String schoolId = "67ca9167d56ec91fc9539234";
        gradeSystem.setSchoolId(schoolId);
        String validationError = this.validateGradeForSchool(gradeSystem, schoolId, null);
        if (validationError != null) {
            return new ResponseEntity<>(new ResponseMessage(validationError, null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        GradeSystem savedGrade = this.repository.save(gradeSystem);
        return new ResponseEntity<>(new ResponseMessage("Grade created successfully", savedGrade), (HttpStatusCode) HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseMessage> getAllGrades() {
        String schoolId = "67ca9167d56ec91fc9539234";
        List<GradeSystem> grades = this.repository.findBySchoolId(schoolId);
        return new ResponseEntity<>(new ResponseMessage("Grades retrieved successfully", grades), (HttpStatusCode) HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> updateGrade(String id, GradeSystem gradeSystem) {
        String schoolId = "67ca9167d56ec91fc9539234";
        return this.updateGradeWithSchool(id, gradeSystem, schoolId);
    }

    public ResponseEntity<ResponseMessage> deleteGrade(String id) {
        String schoolId = "67ca9167d56ec91fc9539234";
        Optional<GradeSystem> gradeOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (gradeOpt.isPresent()) {
            this.repository.deleteById(id);
            return new ResponseEntity<>(new ResponseMessage("Grade deleted successfully", null), (HttpStatusCode) HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("Grade not found", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseMessage> createGradeWithSchool(GradeSystem gradeSystem, String schoolId) {
        String validationError = this.validateGradeForSchool(gradeSystem, schoolId, null);
        if (validationError != null) {
            return new ResponseEntity<>(new ResponseMessage(validationError, null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        gradeSystem.setSchoolId(schoolId);
        GradeSystem savedGrade = this.repository.save(gradeSystem);
        return new ResponseEntity<>(new ResponseMessage("Grade created successfully for school", savedGrade), (HttpStatusCode) HttpStatus.CREATED);
    }

    public List<GradeSystem> getAllGradeBySchool(String schoolId) {
        return this.repository.findBySchoolId(schoolId);
    }

    public ResponseEntity<ResponseMessage> getAllGradesBySchool(String schoolId) {
        List<GradeSystem> grades = this.repository.findBySchoolId(schoolId);
        return new ResponseEntity<>(new ResponseMessage("Grades retrieved successfully for school", grades), (HttpStatusCode) HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> updateGradeWithSchool(String id, GradeSystem gradeSystem, String schoolId) {
        Optional<GradeSystem> existingOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (existingOpt.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("Grade not found or does not belong to this school", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
        }
        String validationError = this.validateGradeForSchool(gradeSystem, schoolId, id);
        if (validationError != null) {
            return new ResponseEntity<>(new ResponseMessage(validationError, null), (HttpStatusCode) HttpStatus.BAD_REQUEST);
        }
        GradeSystem existingGrade = existingOpt.get();
        existingGrade.setScaleStartMarks(gradeSystem.getScaleStartMarks());
        existingGrade.setScaleEndMarks(gradeSystem.getScaleEndMarks());
        existingGrade.setLetterGrade(gradeSystem.getLetterGrade());
        existingGrade.setPerformanceIndicator(gradeSystem.getPerformanceIndicator());
        existingGrade.setSchoolId(schoolId);
        this.repository.save(existingGrade);
        return new ResponseEntity<>(new ResponseMessage("Grade updated successfully for school", existingGrade), (HttpStatusCode) HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> deleteGradeWithSchool(String id, String schoolId) {
        Optional<GradeSystem> gradeOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (gradeOpt.isPresent()) {
            this.repository.deleteById(id);
            return new ResponseEntity<>(new ResponseMessage("Grade deleted successfully for school", null), (HttpStatusCode) HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("Grade not found or does not belong to this school", null), (HttpStatusCode) HttpStatus.NOT_FOUND);
    }
}
