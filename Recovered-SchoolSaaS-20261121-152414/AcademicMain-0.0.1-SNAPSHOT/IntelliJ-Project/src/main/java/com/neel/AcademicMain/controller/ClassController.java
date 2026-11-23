/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.AcademicMain.controller.ClassController
 *  com.neel.AcademicMain.dto.ApiResponseDto
 *  com.neel.AcademicMain.models.ClassEntity
 *  com.neel.AcademicMain.services.ClassService
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
 *  org.springframework.web.server.ResponseStatusException
 */
package com.neel.AcademicMain.controller;

import com.neel.AcademicMain.dto.ApiResponseDto;
import com.neel.AcademicMain.models.ClassEntity;
import com.neel.AcademicMain.services.ClassService;
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
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins={"*"})
@RestController
@RequestMapping(value={"/api/v1/Academic"})
public class ClassController {
    @Autowired
    private ClassService classService;

    @GetMapping(value={"/classes"})
    public List<ClassEntity> getAllClasses() {
        return this.classService.getAllClasses();
    }

    @GetMapping(value={"/classes/{schoolId}"})
    public ResponseEntity<ApiResponseDto> getClassesBySchoolId(@PathVariable String schoolId) {
        List<ClassEntity> classes = this.classService.getClassesBySchoolId(schoolId);
        if (classes.isEmpty()) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body(new ApiResponseDto(Boolean.valueOf(false), "No classes found for this school!", null));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Classes fetched successfully!", classes));
    }

    @PostMapping(value={"/classes/{schoolId}"})
    public ResponseEntity<ApiResponseDto> createClass(@PathVariable String schoolId, @RequestBody ClassEntity classEntity) {
        try {
            classEntity.setSchoolId(schoolId);
            boolean classExists = this.classService.checkIfClassExists(classEntity.getClassName(), schoolId);
            if (classExists) {
                return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(new ApiResponseDto(Boolean.valueOf(false), "Class already exists (either as universal or school-specific class)", null));
            }
            ClassEntity addedClass = this.classService.addClass(classEntity);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Class created successfully!", addedClass));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(new ApiResponseDto(Boolean.valueOf(false), e.getMessage(), null));
        }
    }

    @PutMapping(value={"/classes/{id}/{schoolId}"})
    public ResponseEntity<ApiResponseDto> updateClasses(@PathVariable String schoolId, @PathVariable String id, @RequestBody ClassEntity classEntity) {
        ClassEntity updated = this.classService.updateClasses(schoolId, id, classEntity);
        return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Class updated successfully!", updated));
    }

    @PutMapping(value={"/classes/{id}/"})
    public ResponseEntity<String> updateClassesBySupAdmin(@PathVariable String id, @RequestBody ClassEntity classEntity) {
        try {
            this.classService.updateClassesBySupAdmin(id, classEntity);
            return ResponseEntity.ok("Class updated successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value={"/classes/{id}/{schoolId}"})
    public ResponseEntity<String> deleteSection(@PathVariable String schoolId, @PathVariable String id) {
        try {
            this.classService.deleteClasses(schoolId, id);
            return ResponseEntity.ok("CLass deleted successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value={"/classes/universal"})
    public ResponseEntity<ApiResponseDto> createUniversalClass(@RequestBody ClassEntity classEntity) {
        try {
            classEntity.setUniversal(true);
            ClassEntity addedClass = this.classService.addClass(classEntity);
            return ResponseEntity.status((HttpStatusCode)HttpStatus.OK).body(new ApiResponseDto(Boolean.valueOf(true), "Universal class created successfully!", addedClass));
        }
        catch (Exception e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.CONFLICT).body(new ApiResponseDto(Boolean.valueOf(false), e.getMessage(), null));
        }
    }

    @GetMapping(value={"/classes/universal"})
    public ResponseEntity<List<ClassEntity>> getAllUniversalClasses() {
        List<ClassEntity> universalClasses = this.classService.getAllUniversalClasses();
        return ResponseEntity.ok(universalClasses);
    }

    @GetMapping(value={"/classes/all/{schoolId}"})
    public ResponseEntity<List<ClassEntity>> getAllClassesForSchool(@PathVariable String schoolId) {
        List<ClassEntity> classes = this.classService.getAllClassesForSchool(schoolId);
        return ResponseEntity.ok(classes);
    }

    @GetMapping(value={"/class/{className}/schoolId/{schoolId}"})
    public ResponseEntity<?> getClassByClassNameAndSchoolId(@PathVariable String className, @PathVariable String schoolId) {
        List<ClassEntity> exists = this.classService.getClassByClassNameAndSchoolId(className, schoolId);
        if (exists.isEmpty()) {
            throw new ResponseStatusException((HttpStatusCode)HttpStatus.NOT_FOUND, "Class not found for schoolId: " + schoolId);
        }
        return ResponseEntity.ok(true);
    }

    @DeleteMapping(value={"/classes/{id}"})
    public ResponseEntity<String> deleteClassBySupAdmin(@PathVariable String id) {
        try {
            this.classService.deleteBySupAdminClasses(id);
            return ResponseEntity.ok("CLass deleted successfully!");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

