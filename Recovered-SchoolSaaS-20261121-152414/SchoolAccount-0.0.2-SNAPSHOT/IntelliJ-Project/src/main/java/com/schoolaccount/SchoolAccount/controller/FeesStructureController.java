/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.schoolaccount.SchoolAccount.controller.FeesStructureController
 *  com.schoolaccount.SchoolAccount.model.FeesStructure
 *  com.schoolaccount.SchoolAccount.model.ResponseMessage
 *  com.schoolaccount.SchoolAccount.service.FeesStructureService
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

import com.schoolaccount.SchoolAccount.model.FeesStructure;
import com.schoolaccount.SchoolAccount.model.ResponseMessage;
import com.schoolaccount.SchoolAccount.service.FeesStructureService;
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
public class FeesStructureController {
    @Autowired
    private FeesStructureService feesStructureService;

    @PostMapping(value={"/fees-structures"})
    public ResponseEntity<ResponseMessage> createFeesStructure(@RequestBody FeesStructure feesStructure) {
        ResponseMessage response = this.feesStructureService.createFeesStructure(feesStructure);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/fees-structures"})
    public ResponseEntity<ResponseMessage> getAllFeesStructures() {
        ResponseMessage response = this.feesStructureService.getAllFeesStructures();
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/fees-structures/id/{id}"})
    public ResponseEntity<ResponseMessage> getFeesStructureById(@PathVariable String id) {
        ResponseMessage response = this.feesStructureService.getFeesStructureById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/fees-structures/class/{className}/section/{section}"})
    public ResponseEntity<ResponseMessage> getFeesStructureByClassAndSection(@PathVariable String className, @PathVariable String section) {
        ResponseMessage response = this.feesStructureService.getFeesStructureByClassAndSection(className, section);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value={"/fees-structures/id/{id}"})
    public ResponseEntity<ResponseMessage> updateFeesStructure(@PathVariable String id, @RequestBody FeesStructure updatedFeesStructure) {
        ResponseMessage response = this.feesStructureService.updateFeesStructure(id, updatedFeesStructure);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value={"/fees-structures/{id}"})
    public ResponseEntity<ResponseMessage> deleteFeesStructure(@PathVariable String id) {
        ResponseMessage response = this.feesStructureService.deleteFeesStructure(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value={"/fees-structure/{schoolId}"})
    public ResponseEntity<ResponseMessage> createFeesStructureWithSchool(@PathVariable String schoolId, @RequestBody FeesStructure feesStructure) {
        ResponseMessage response = this.feesStructureService.createFeesStructureWithSchool(feesStructure, schoolId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/fees-structure/all/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllFeesStructuresBySchool(@PathVariable String schoolId) {
        ResponseMessage response = this.feesStructureService.getAllFeesStructuresBySchool(schoolId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value={"/fees-structure/{className}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getFeesStructureByClassAndSchool(@PathVariable String schoolId, @PathVariable String className) {
        ResponseMessage response = this.feesStructureService.getFeesStructureByClassAndSchool(className, schoolId);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value={"/fees-structure/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateFeesStructureWithSchool(@PathVariable String schoolId, @PathVariable String id, @RequestBody FeesStructure updatedFeesStructure) {
        ResponseMessage response = this.feesStructureService.updateFeesStructureWithSchool(id, updatedFeesStructure, schoolId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value={"/fees-structure/delete/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteFeesStructureWithSchool(@PathVariable String schoolId, @PathVariable String id) {
        ResponseMessage response = this.feesStructureService.deleteFeesStructureWithSchool(id, schoolId);
        return ResponseEntity.ok(response);
    }
}

