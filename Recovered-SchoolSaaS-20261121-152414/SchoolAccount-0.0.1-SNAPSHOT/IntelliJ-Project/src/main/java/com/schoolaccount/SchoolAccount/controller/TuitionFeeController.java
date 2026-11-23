/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.TuitionFeeController
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.TuitionFeeService
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
package com.schoolaccount.SchoolAccount.controller;

import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.TuitionFeeService;
import java.util.Map;
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
@RequestMapping(value={"/api"})
public class TuitionFeeController {
    @Autowired
    private TuitionFeeService tuitionFeeService;

    @PostMapping(value={"/tuition-fees"})
    public ResponseEntity<ResponseMessage> createTuitionFee(@RequestBody Map<String, Object> request) {
        String className = (String)request.get("className");
        double monthlyFee = Double.parseDouble(request.get("monthlyFee").toString());
        ResponseMessage response = this.tuitionFeeService.createTuitionFee(className, monthlyFee);
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/tuition-fees"})
    public ResponseEntity<ResponseMessage> getAllTuitionFees() {
        ResponseMessage response = this.tuitionFeeService.getAllTuitionFees();
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/tuition-fees/id/{id}"})
    public ResponseEntity<ResponseMessage> getTuitionFeeById(@PathVariable String id) {
        ResponseMessage response = this.tuitionFeeService.getTuitionFeeById(id);
        return ResponseEntity.ok((Object)response);
    }

    @PutMapping(value={"/tuition-fees/id/{id}"})
    public ResponseEntity<ResponseMessage> updateTuitionFee(@PathVariable String id, @RequestBody Map<String, Object> request) {
        double monthlyFee = Double.parseDouble(request.get("monthlyFee").toString());
        ResponseMessage response = this.tuitionFeeService.updateTuitionFee(id, monthlyFee);
        return ResponseEntity.ok((Object)response);
    }

    @DeleteMapping(value={"/tuition-fees/{id}"})
    public ResponseEntity<ResponseMessage> deleteTuitionFee(@PathVariable String id) {
        ResponseMessage response = this.tuitionFeeService.deleteTuitionFee(id);
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/tuition-fees/class/{className}"})
    public ResponseEntity<ResponseMessage> getFeeByClassName(@PathVariable String className) {
        ResponseMessage response = this.tuitionFeeService.getFeeByClassName(className);
        return ResponseEntity.ok((Object)response);
    }

    @PostMapping(value={"/tuition-fees/{schoolId}"})
    public ResponseEntity<ResponseMessage> createTuitionFeeWithSchool(@PathVariable String schoolId, @RequestBody Map<String, Object> request) {
        String className = (String)request.get("className");
        double monthlyFee = Double.parseDouble(request.get("monthlyFee").toString());
        ResponseMessage response = this.tuitionFeeService.createTuitionFeeWithSchool(className, monthlyFee, schoolId);
        System.out.println("i m from response : " + response);
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/tuition-fees/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllTuitionFeesBySchool(@PathVariable String schoolId) {
        ResponseMessage response = this.tuitionFeeService.getAllTuitionFeesBySchool(schoolId);
        return ResponseEntity.ok((Object)response);
    }

    @PutMapping(value={"/tuition-fees/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateTuitionFeeWithSchool(@PathVariable String schoolId, @PathVariable String id, @RequestBody Map<String, Object> request) {
        double monthlyFee = Double.parseDouble(request.get("monthlyFee").toString());
        ResponseMessage response = this.tuitionFeeService.updateTuitionFeeWithSchool(id, monthlyFee, schoolId);
        return ResponseEntity.ok((Object)response);
    }

    @DeleteMapping(value={"/tuition-fees/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteTuitionFeeWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        ResponseMessage response = this.tuitionFeeService.deleteTuitionFeeWithSchool(id, schoolId);
        return ResponseEntity.ok((Object)response);
    }

    @GetMapping(value={"/tuition-fees/by-class/{className}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getFeeByClassNameAndSchool(@PathVariable String schoolId, @PathVariable String className) {
        ResponseMessage response = this.tuitionFeeService.getFeeByClassNameAndSchool(className, schoolId);
        return ResponseEntity.ok((Object)response);
    }
}

