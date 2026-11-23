/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.controller.ClassTimePageController
 *  com.neel.RoutineMain.model.ClassTimePage
 *  com.neel.RoutineMain.model.ResponseMessage
 *  com.neel.RoutineMain.service.ClassTimePageService
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
package com.neel.RoutineMain.controller;

import com.neel.RoutineMain.model.ClassTimePage;
import com.neel.RoutineMain.model.ResponseMessage;
import com.neel.RoutineMain.service.ClassTimePageService;
import java.util.List;
import java.util.Optional;
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
@RequestMapping(value={"/api/v1/routine/class-time"})
public class ClassTimePageController {
    @Autowired
    private ClassTimePageService service;

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllClassTimes() {
        List classTimes = this.service.getAllClassTimes();
        return ResponseEntity.ok(new ResponseMessage("Class times retrieved successfully", classTimes));
    }

    @GetMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> getClassTimeById(@PathVariable String id) {
        ClassTimePage classTime = this.service.getClassTimeById(id);
        if (classTime != null) {
            return ResponseEntity.ok(new ResponseMessage("Class time retrieved successfully", classTime));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Class time not found", null));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createClassTime(@RequestBody ClassTimePage classTime) {
        Optional existPeriod = this.service.getAllClassTimesBySchoolAndPeriod(classTime.getSchoolId(), classTime.getPeriods());
        if (existPeriod.isPresent()) {
            return ResponseEntity.status((int)400).body(new ResponseMessage("Class time period already exists for this school", null));
        }
        ClassTimePage createdClassTime = this.service.createClassTime(classTime);
        return ResponseEntity.status((int)201).body(new ResponseMessage("Class time created successfully", createdClassTime));
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateClassTime(@PathVariable String id, @RequestBody ClassTimePage updatedClassTime) {
        ClassTimePage updated = this.service.updateClassTime(id, updatedClassTime);
        if (updated != null) {
            return ResponseEntity.ok(new ResponseMessage("Class time updated successfully", updated));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Class time not found", null));
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteClassTime(@PathVariable String id) {
        try {
            this.service.deleteClassTime(id);
            return ResponseEntity.ok(new ResponseMessage("Class time deleted successfully", null));
        }
        catch (Exception e) {
            return ResponseEntity.status((int)404).body(new ResponseMessage("Class time not found", null));
        }
    }

    @GetMapping(value={"/all/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllClassTimesBySchool(@PathVariable String schoolId) {
        List classTimes = this.service.getAllClassTimesBySchool(schoolId);
        return ResponseEntity.ok(new ResponseMessage("Class times retrieved successfully for school", classTimes));
    }

    @GetMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> getClassTimeByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        ClassTimePage classTime = this.service.getClassTimeByIdAndSchool(id, schoolId);
        if (classTime != null) {
            return ResponseEntity.ok(new ResponseMessage("Class time retrieved successfully for school", classTime));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Class time not found or does not belong to this school", null));
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> createClassTimeWithSchool(@RequestBody ClassTimePage classTime, @PathVariable String schoolId) {
        System.out.println(" i m from schoolId : " + schoolId);
        Optional existPeriod = this.service.getAllClassTimesBySchoolAndPeriod(schoolId, classTime.getPeriods());
        System.out.println("i m from exist periods :  " + existPeriod);
        if (existPeriod.isPresent()) {
            return ResponseEntity.status((int)409).body(new ResponseMessage("Class time period already exists for this school", null));
        }
        ClassTimePage createdClassTime = this.service.createClassTimeWithSchool(classTime, schoolId);
        return ResponseEntity.status((int)201).body(new ResponseMessage("Class time created successfully for school", createdClassTime));
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateClassTimeWithSchool(@PathVariable String id, @RequestBody ClassTimePage updatedClassTime, @PathVariable String schoolId) {
        ClassTimePage updated = this.service.updateClassTimeWithSchool(id, updatedClassTime, schoolId);
        if (updated != null) {
            return ResponseEntity.ok(new ResponseMessage("Class time updated successfully for school", updated));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Class time not found or does not belong to this school", null));
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteClassTimeWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        boolean deleted = this.service.deleteClassTimeWithSchool(id, schoolId);
        if (deleted) {
            return ResponseEntity.ok(new ResponseMessage("Class time deleted successfully for school", null));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Class time not found or does not belong to this school", null));
    }
}

