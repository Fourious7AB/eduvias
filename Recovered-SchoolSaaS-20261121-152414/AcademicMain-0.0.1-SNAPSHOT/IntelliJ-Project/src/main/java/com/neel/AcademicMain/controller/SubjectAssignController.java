/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.controller.SubjectAssignController
 *  com.neel.AcademicMain.dto.ApiResponseDto
 *  com.neel.AcademicMain.models.ClassEntity
 *  com.neel.AcademicMain.models.Subject
 *  com.neel.AcademicMain.models.Subject_Assign
 *  com.neel.AcademicMain.repo.ClassRepository
 *  com.neel.AcademicMain.repo.SubjectRepository
 *  com.neel.AcademicMain.services.SubjectAssignService
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
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.AcademicMain.controller;

import com.neel.AcademicMain.dto.ApiResponseDto;
import com.neel.AcademicMain.models.ClassEntity;
import com.neel.AcademicMain.models.Subject;
import com.neel.AcademicMain.models.Subject_Assign;
import com.neel.AcademicMain.repo.ClassRepository;
import com.neel.AcademicMain.repo.SubjectRepository;
import com.neel.AcademicMain.services.SubjectAssignService;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping(value={"/api/v1/Academic"})
public class SubjectAssignController {
    @Autowired
    private SubjectAssignService subjectAssignService;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    @GetMapping(value={"/subject-assign"})
    public List<Subject_Assign> getAllSubjectAssign() {
        return this.subjectAssignService.getAllSubjectAssign();
    }

    @GetMapping(value={"/subject-assign/{schoolId}"})
    public ResponseEntity<List<Subject_Assign>> getSubjectAssignBySchoolId(@PathVariable String schoolId) {
        List<Subject_Assign> subjectAssigns = this.subjectAssignService.getSubjectAssignBySchoolId(schoolId);
        if (subjectAssigns.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(subjectAssigns);
    }

    @PostMapping(value={"/subject-assign/{schoolId}"})
    public ResponseEntity<ApiResponseDto> createSubjectAssign(@PathVariable String schoolId, @RequestBody Subject_Assign subject_assign) {
        try {
            subject_assign.setSchoolId(schoolId);
            ClassEntity classObj = null;
            Optional<ClassEntity> schoolClass = this.classRepository.findByClassNameAndSchoolId(subject_assign.getClassId(), schoolId);
            Optional<ClassEntity> universalClass = this.classRepository.findByClassNameIgnoreCaseAndUniversal(subject_assign.getClassId(), true);
            if (schoolClass.isPresent()) {
                classObj = schoolClass.get();
            } else if (universalClass.isPresent()) {
                classObj = universalClass.get();
            } else {
                throw new IllegalArgumentException("Class not found for this school");
            }
            Subject subjectObj = null;
            Optional<Subject> schoolSubject = this.subjectRepository.findBySubjectNameAndSchoolId(subject_assign.getSubjectId(), schoolId);
            Optional<Subject> universalSubject = this.subjectRepository.findBySubjectName(subject_assign.getSubjectId());
            if (schoolSubject.isPresent()) {
                subjectObj = schoolSubject.get();
            } else if (universalSubject.isPresent() && universalSubject.get().isUniversal()) {
                subjectObj = universalSubject.get();
            } else {
                throw new IllegalArgumentException("Subject not found for this school");
            }
            boolean exists = this.subjectAssignService.checkIfAssignmentExists(classObj.getClassName(), subjectObj.getSubjectName(), schoolId);
            if (exists) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(new ApiResponseDto(Boolean.valueOf(false), "This subject is already assigned to this class", null));
            }
            String subjectCode = classObj.getClass_Code() + "-" + subjectObj.getSubject_index();
            subject_assign.setSubjectCode(subjectCode);
            subject_assign.setClassId(classObj.getClassName());
            subject_assign.setSubjectId(subjectObj.getSubjectName());
            Subject_Assign savedAssignment = this.subjectAssignService.addSubjectAssign(subject_assign);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Subject assignment created successfully!", savedAssignment));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body(new ApiResponseDto(Boolean.valueOf(false), e.getMessage(), null));
        }
    }

    @PutMapping(value={"/subject-assign/{id}/{schoolId}"})
    public ResponseEntity<ApiResponseDto> updateSubjectAssign(@PathVariable String schoolId, @PathVariable String id, @RequestBody Subject_Assign subject_assign) {
        try {
            Subject_Assign updatedAssignment = this.subjectAssignService.updateSubjectAssign(schoolId, id, subject_assign);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Subject assignment updated successfully!", updatedAssignment));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body(new ApiResponseDto(Boolean.valueOf(false), e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/subject-assign/{id}/{schoolId}"})
    public ResponseEntity<ApiResponseDto> deleteSubjectAssign(@PathVariable String schoolId, @PathVariable String id) {
        try {
            this.subjectAssignService.deleteSubjectAssign(schoolId, id);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Subject assignment deleted successfully!", null));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body(new ApiResponseDto(Boolean.valueOf(false), e.getMessage(), null));
        }
    }

    @GetMapping(value={"/subject-assign/{schoolId}/search"})
    public ResponseEntity<ApiResponseDto> getSubjectByClassNameAndSchool(@PathVariable String schoolId, @RequestParam String classId) {
        try {
            List<Subject_Assign> subjectAssigns = this.subjectAssignService.getSubjectByClassNameAndSchool(classId, schoolId);
            if (subjectAssigns.isEmpty()) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ApiResponseDto(Boolean.valueOf(false), "No subject assignments found for this class", null));
            }
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Subject assignments fetched successfully!", subjectAssigns));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body(new ApiResponseDto(Boolean.valueOf(false), e.getMessage(), null));
        }
    }
}

