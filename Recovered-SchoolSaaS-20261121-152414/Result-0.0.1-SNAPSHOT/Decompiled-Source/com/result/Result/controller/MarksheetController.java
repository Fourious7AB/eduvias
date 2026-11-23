/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.result.Result.controller.MarksheetController
 *  com.result.Result.model.ResponseMessage
 *  com.result.Result.service.MarksheetService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RequestParam
 *  org.springframework.web.bind.annotation.RestController
 */
package com.result.Result.controller;

import com.result.Result.model.ResponseMessage;
import com.result.Result.service.MarksheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins={"*"})
@RequestMapping(value={"/api/marksheet"})
public class MarksheetController {
    @Autowired
    private MarksheetService marksheetService;

    @GetMapping(value={"/student"})
    public ResponseEntity<ResponseMessage> getStudentMarksheet(@RequestParam String className, @RequestParam String section, @RequestParam int roll, @RequestParam String examType) {
        return this.marksheetService.generateStudentMarksheet(className, section, roll, examType);
    }

    @GetMapping(value={"/student/{schoolId}"})
    public ResponseEntity<ResponseMessage> getStudentMarksheetWithSchool(@RequestParam String className, @RequestParam String section, @RequestParam int roll, @RequestParam String examType, @PathVariable String schoolId) {
        return this.marksheetService.generateStudentMarksheetWithSchool(className, section, roll, examType, schoolId);
    }

    @GetMapping(value={"/class-section"})
    public ResponseEntity<ResponseMessage> getClassSectionMarksheets(@RequestParam String className, @RequestParam String section, @RequestParam String examType) {
        return this.marksheetService.generateClassSectionMarksheets(className, section, examType);
    }

    @GetMapping(value={"/class-section/{schoolId}"})
    public ResponseEntity<ResponseMessage> getClassSectionMarksheetsWithSchool(@RequestParam String className, @RequestParam String section, @RequestParam String examType, @PathVariable String schoolId) {
        return this.marksheetService.generateClassSectionMarksheetsWithSchool(className, section, examType, schoolId);
    }
}

