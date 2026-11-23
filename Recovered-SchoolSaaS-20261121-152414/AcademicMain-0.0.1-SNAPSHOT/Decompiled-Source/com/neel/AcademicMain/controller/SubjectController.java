/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.controller.SubjectController
 *  com.neel.AcademicMain.dto.ApiResponseDto
 *  com.neel.AcademicMain.models.Subject
 *  com.neel.AcademicMain.services.SubjectService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestBody
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.AcademicMain.controller;

import com.neel.AcademicMain.dto.ApiResponseDto;
import com.neel.AcademicMain.models.Subject;
import com.neel.AcademicMain.services.SubjectService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping(value={"/api/v1/Academic"})
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping(value={"/subjects"})
    public List<Subject> getAllSubjects() {
        return this.subjectService.getAllSubjects();
    }

    @GetMapping(value={"/subjects/{schoolId}"})
    public ResponseEntity<ApiResponseDto> getSubjectsBySchoolId(@PathVariable String schoolId) {
        List subjects = this.subjectService.getSubjectsBySchoolId(schoolId);
        if (subjects.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body((Object)new ApiResponseDto(Boolean.valueOf(true), "Subjects fetched successfully!", (Object)subjects));
    }

    @GetMapping(value={"/subjects/universal"})
    public ResponseEntity<ApiResponseDto> getUniversalSubjects() {
        List subjects = this.subjectService.getUniversalSubjects();
        if (subjects.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ApiResponseDto(Boolean.valueOf(false), "No universal subjects found!", null));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body((Object)new ApiResponseDto(Boolean.valueOf(true), "Subjects fetched successfully!", (Object)subjects));
    }

    @GetMapping(value={"/subjects/all/{schoolId}"})
    public ResponseEntity<ApiResponseDto> getAllSubjectsForSchool(@PathVariable String schoolId) {
        List subjects = this.subjectService.getSubjectsForSchool(schoolId);
        if (subjects.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ApiResponseDto(Boolean.valueOf(false), "No subjects found for this school!", null));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body((Object)new ApiResponseDto(Boolean.valueOf(true), "Subjects fetched successfully!", (Object)subjects));
    }

    @PostMapping(value={"/subjects/{schoolId}"})
    public ResponseEntity<ApiResponseDto> createSubject(@PathVariable String schoolId, @RequestBody Subject subject) {
        try {
            subject.setSchoolId(schoolId);
            this.subjectService.addSubject(subject);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body((Object)new ApiResponseDto(Boolean.valueOf(true), "Subject created successfully!", (Object)subject));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ApiResponseDto(Boolean.valueOf(false), "Subject is already exist", null));
        }
    }

    @PostMapping(value={"/subjects/universal"})
    public ResponseEntity<ApiResponseDto> createUniversalSubject(@RequestBody Subject subject) {
        try {
            Subject createdSubject = this.subjectService.addUniversalSubject(subject);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.CREATED).body((Object)new ApiResponseDto(Boolean.valueOf(true), "Universal subject created successfully!", (Object)createdSubject));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ApiResponseDto(Boolean.valueOf(false), "Subject is already exist", null));
        }
    }

    @PutMapping(value={"/subjects/{id}/{schoolId}"})
    public ResponseEntity<ApiResponseDto> updateSubject(@PathVariable String schoolId, @PathVariable String id, @RequestBody Subject subject) {
        try {
            this.subjectService.updateSubject(schoolId, id, subject);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body((Object)new ApiResponseDto(Boolean.valueOf(true), "Subject updated successfully!", (Object)subject));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ApiResponseDto(Boolean.valueOf(false), "Subject is already exist", null));
        }
    }

    @DeleteMapping(value={"/subjects/{id}/{schoolId}"})
    public ResponseEntity<ApiResponseDto> deleteSubject(@PathVariable String schoolId, @PathVariable String id) {
        try {
            this.subjectService.deleteSubject(schoolId, id);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body((Object)new ApiResponseDto(Boolean.valueOf(true), "Subject deleted successfully!", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body((Object)new ApiResponseDto(Boolean.valueOf(false), "Subject is already exist", null));
        }
    }
}

