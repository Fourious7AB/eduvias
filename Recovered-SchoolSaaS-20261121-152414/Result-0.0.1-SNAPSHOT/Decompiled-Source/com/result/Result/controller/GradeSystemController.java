/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.controller.GradeSystemController
 *  com.result.Result.model.GradeSystem
 *  com.result.Result.model.ResponseMessage
 *  com.result.Result.service.GradeSystemService
 *  org.springframework.beans.factory.annotation.Autowired
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
package com.result.Result.controller;

import com.result.Result.model.GradeSystem;
import com.result.Result.model.ResponseMessage;
import com.result.Result.service.GradeSystemService;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/grades"})
public class GradeSystemController {
    @Autowired
    private GradeSystemService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> createGrade(@RequestBody GradeSystem gradeSystem) {
        return this.service.createGrade(gradeSystem);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllGrades() {
        return this.service.getAllGrades();
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateGrade(@PathVariable String id, @RequestBody GradeSystem gradeSystem) {
        return this.service.updateGrade(id, gradeSystem);
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteGrade(@PathVariable String id) {
        return this.service.deleteGrade(id);
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> createGradeWithSchool(@RequestBody GradeSystem gradeSystem, @PathVariable String schoolId) {
        return this.service.createGradeWithSchool(gradeSystem, schoolId);
    }

    @GetMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllGradesBySchool(@PathVariable String schoolId) {
        return this.service.getAllGradesBySchool(schoolId);
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateGradeWithSchool(@PathVariable String id, @RequestBody GradeSystem gradeSystem, @PathVariable String schoolId) {
        return this.service.updateGradeWithSchool(id, gradeSystem, schoolId);
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteGradeWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        return this.service.deleteGradeWithSchool(id, schoolId);
    }
}

