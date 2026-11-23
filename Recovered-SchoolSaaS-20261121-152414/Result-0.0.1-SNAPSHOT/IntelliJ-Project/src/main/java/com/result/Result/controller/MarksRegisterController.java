/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.controller.MarksRegisterController
 *  com.result.Result.model.MarksRegister
 *  com.result.Result.model.ResponseMessage
 *  com.result.Result.service.MarksRegisterService
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
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.result.Result.controller;

import com.result.Result.model.MarksRegister;
import com.result.Result.model.ResponseMessage;
import com.result.Result.service.MarksRegisterService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/marks-register"})
public class MarksRegisterController {
    @Autowired
    private MarksRegisterService service;

    @PostMapping
    public ResponseEntity<ResponseMessage> createMarksRegister(@RequestBody MarksRegister marksRegister) {
        return this.service.createMarksRegister(marksRegister);
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> getMarksRegister(@PathVariable String id) {
        return this.service.getMarksRegister(id);
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateMarksRegister(@PathVariable String id, @RequestBody MarksRegister marksRegister) {
        return this.service.updateMarksRegister(id, marksRegister);
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteMarksRegister(@PathVariable String id) {
        return this.service.deleteMarksRegister(id);
    }

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllMarksRegisters() {
        return this.service.getAllMarksRegisters();
    }

    @GetMapping(value={"/search"})
    public ResponseEntity<ResponseMessage> searchMarks(@RequestParam String className, @RequestParam String section, @RequestParam int roll, @RequestParam String examType) {
        return this.service.searchMarks(className, section, roll, examType);
    }

    @GetMapping(value={"/search/subject"})
    public ResponseEntity<ResponseMessage> searchMarksSubject(@RequestParam String className, @RequestParam String section, @RequestParam String subject, @RequestParam String examType) {
        return this.service.searchMarksSubject(className, section, subject, examType);
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> createMarksRegisterWithSchool(@RequestBody MarksRegister marksRegister, @PathVariable String schoolId) {
        return this.service.createMarksRegisterWithSchool(marksRegister, schoolId);
    }

    @GetMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getMarksRegisterWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        return this.service.getMarksRegisterWithSchool(id, schoolId);
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateMarksRegisterWithSchool(@PathVariable String id, @RequestBody MarksRegister marksRegister, @PathVariable String schoolId) {
        return this.service.updateMarksRegisterWithSchool(id, marksRegister, schoolId);
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteMarksRegisterWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        return this.service.deleteMarksRegisterWithSchool(id, schoolId);
    }

    @GetMapping(value={"/all/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllMarksRegistersBySchool(@PathVariable String schoolId) {
        return this.service.getAllMarksRegistersBySchool(schoolId);
    }

    @GetMapping(value={"/{schoolId}/search"})
    public ResponseEntity<ResponseMessage> searchMarksWithSchool(@RequestParam String className, @RequestParam String section, @RequestParam int roll, @RequestParam String examType, @PathVariable String schoolId) {
        return this.service.searchMarksWithSchool(className, section, roll, examType, schoolId);
    }

    @GetMapping(value={"/{schoolId}/get/search"})
    public ResponseEntity<ResponseMessage> searchMarksSubjectWithSchool(@RequestParam String className, @RequestParam String section, @RequestParam String subject, @RequestParam String examType, @PathVariable String schoolId) {
        return this.service.searchMarksSubjectWithSchool(className, section, subject, examType, schoolId);
    }
}

