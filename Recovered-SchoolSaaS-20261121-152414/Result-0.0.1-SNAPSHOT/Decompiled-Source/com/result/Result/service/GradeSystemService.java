/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.model.GradeSystem
 *  com.result.Result.model.ResponseMessage
 *  com.result.Result.repo.GradeSystemRepository
 *  com.result.Result.service.GradeSystemService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.stereotype.Service
 */
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

    public ResponseEntity<ResponseMessage> createGrade(GradeSystem gradeSystem) {
        gradeSystem.setSchoolId("67ca9167d56ec91fc9539234");
        if (this.repository.findByPerformanceIndicatorAndSchoolId(gradeSystem.getPerformanceIndicator(), gradeSystem.getSchoolId()).isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Performance Indicator already exists", null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        GradeSystem savedGrade = (GradeSystem)this.repository.save((Object)gradeSystem);
        return new ResponseEntity((Object)new ResponseMessage("Grade created successfully", (Object)savedGrade), (HttpStatusCode)HttpStatus.CREATED);
    }

    public ResponseEntity<ResponseMessage> getAllGrades() {
        String schoolId = "67ca9167d56ec91fc9539234";
        List grades = this.repository.findBySchoolId(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Grades retrieved successfully", (Object)grades), (HttpStatusCode)HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> updateGrade(String id, GradeSystem gradeSystem) {
        String schoolId = "67ca9167d56ec91fc9539234";
        gradeSystem.setSchoolId(schoolId);
        return this.repository.findByIdAndSchoolId(id, schoolId).map(existingGrade -> {
            existingGrade.setScaleStartMarks(gradeSystem.getScaleStartMarks());
            existingGrade.setScaleEndMarks(gradeSystem.getScaleEndMarks());
            existingGrade.setLetterGrade(gradeSystem.getLetterGrade());
            existingGrade.setPerformanceIndicator(gradeSystem.getPerformanceIndicator());
            existingGrade.setSchoolId(schoolId);
            this.repository.save(existingGrade);
            return new ResponseEntity((Object)new ResponseMessage("Grade updated successfully", existingGrade), (HttpStatusCode)HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity((Object)new ResponseMessage("Grade not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> deleteGrade(String id) {
        String schoolId = "67ca9167d56ec91fc9539234";
        Optional gradeOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (gradeOpt.isPresent()) {
            this.repository.deleteById((Object)id);
            return new ResponseEntity((Object)new ResponseMessage("Grade deleted successfully", null), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Grade not found", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseMessage> createGradeWithSchool(GradeSystem gradeSystem, String schoolId) {
        gradeSystem.setSchoolId(schoolId);
        if (this.repository.findByPerformanceIndicatorAndSchoolId(gradeSystem.getPerformanceIndicator(), schoolId).isPresent()) {
            return new ResponseEntity((Object)new ResponseMessage("Performance Indicator already exists for this school", null), (HttpStatusCode)HttpStatus.BAD_REQUEST);
        }
        GradeSystem savedGrade = (GradeSystem)this.repository.save((Object)gradeSystem);
        return new ResponseEntity((Object)new ResponseMessage("Grade created successfully for school", (Object)savedGrade), (HttpStatusCode)HttpStatus.CREATED);
    }

    public List<GradeSystem> getAllGradeBySchool(String schoolId) {
        return this.repository.findBySchoolId(schoolId);
    }

    public ResponseEntity<ResponseMessage> getAllGradesBySchool(String schoolId) {
        List grades = this.repository.findBySchoolId(schoolId);
        return new ResponseEntity((Object)new ResponseMessage("Grades retrieved successfully for school", (Object)grades), (HttpStatusCode)HttpStatus.OK);
    }

    public ResponseEntity<ResponseMessage> updateGradeWithSchool(String id, GradeSystem gradeSystem, String schoolId) {
        gradeSystem.setSchoolId(schoolId);
        return this.repository.findByIdAndSchoolId(id, schoolId).map(existingGrade -> {
            existingGrade.setScaleStartMarks(gradeSystem.getScaleStartMarks());
            existingGrade.setScaleEndMarks(gradeSystem.getScaleEndMarks());
            existingGrade.setLetterGrade(gradeSystem.getLetterGrade());
            existingGrade.setPerformanceIndicator(gradeSystem.getPerformanceIndicator());
            existingGrade.setSchoolId(schoolId);
            this.repository.save(existingGrade);
            return new ResponseEntity((Object)new ResponseMessage("Grade updated successfully for school", existingGrade), (HttpStatusCode)HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity((Object)new ResponseMessage("Grade not found or does not belong to this school", null), (HttpStatusCode)HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<ResponseMessage> deleteGradeWithSchool(String id, String schoolId) {
        Optional gradeOpt = this.repository.findByIdAndSchoolId(id, schoolId);
        if (gradeOpt.isPresent()) {
            this.repository.deleteById((Object)id);
            return new ResponseEntity((Object)new ResponseMessage("Grade deleted successfully for school", null), (HttpStatusCode)HttpStatus.OK);
        }
        return new ResponseEntity((Object)new ResponseMessage("Grade not found or does not belong to this school", null), (HttpStatusCode)HttpStatus.NOT_FOUND);
    }
}

