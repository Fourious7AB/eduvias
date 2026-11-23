/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.RoutineMain.controller.ExamTypeController
 *  com.neel.RoutineMain.model.ExamTypePage
 *  com.neel.RoutineMain.model.ResponseMessage
 *  com.neel.RoutineMain.service.ExamTypePageService
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

import com.neel.RoutineMain.model.ExamTypePage;
import com.neel.RoutineMain.model.ResponseMessage;
import com.neel.RoutineMain.service.ExamTypePageService;
import java.util.List;
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
@RequestMapping(value={"/api/v1/routine/exam-type"})
public class ExamTypeController {
    @Autowired
    private ExamTypePageService examTypePageService;

    @GetMapping
    public ResponseEntity<ResponseMessage> getAllExamTypes() {
        List examTypePages = this.examTypePageService.getAllExamTypes();
        return ResponseEntity.ok(new ResponseMessage("Exam Type retrieved successfully", examTypePages));
    }

    @GetMapping(value={"/id/{id}"})
    public ResponseEntity<ResponseMessage> getExamTypeById(@PathVariable String id) {
        ExamTypePage examTypePages = this.examTypePageService.getExamTypeById(id);
        if (examTypePages != null) {
            return ResponseEntity.ok(new ResponseMessage("Exam Type retrieved successfully", examTypePages));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Exam Type not found", null));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createExamType(@RequestBody ExamTypePage examTypePages) {
        ExamTypePage createdExamType = this.examTypePageService.createExamType(examTypePages);
        return ResponseEntity.status((int)201).body(new ResponseMessage("Exam Type created successfully", createdExamType));
    }

    @PutMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> updateExamType(@PathVariable String id, @RequestBody ExamTypePage updatedExamType) {
        ExamTypePage updated = this.examTypePageService.updateExamType(id, updatedExamType);
        if (updated != null) {
            return ResponseEntity.ok(new ResponseMessage("Exam Type updated successfully", updated));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Exam Type not found", null));
    }

    @DeleteMapping(value={"/{id}"})
    public ResponseEntity<ResponseMessage> deleteExamType(@PathVariable String id) {
        try {
            this.examTypePageService.deleteExamType(id);
            return ResponseEntity.ok(new ResponseMessage("Exam Type deleted successfully", null));
        }
        catch (Exception e) {
            return ResponseEntity.status((int)404).body(new ResponseMessage("Exam Type not found", null));
        }
    }

    @GetMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> getAllExamTypesBySchool(@PathVariable String schoolId) {
        List examTypePages = this.examTypePageService.getAllExamTypesBySchool(schoolId);
        return ResponseEntity.ok(new ResponseMessage("Exam Types retrieved successfully for school", examTypePages));
    }

    @GetMapping(value={"/{id}/school/{schoolId}"})
    public ResponseEntity<ResponseMessage> getExamTypeByIdAndSchool(@PathVariable String id, @PathVariable String schoolId) {
        ExamTypePage examTypePage = this.examTypePageService.getExamTypeByIdAndSchool(id, schoolId);
        if (examTypePage != null) {
            return ResponseEntity.ok(new ResponseMessage("Exam Type retrieved successfully for school", examTypePage));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Exam Type not found for this school", null));
    }

    @PostMapping(value={"/{schoolId}"})
    public ResponseEntity<ResponseMessage> createExamTypeWithSchool(@RequestBody ExamTypePage examTypePage, @PathVariable String schoolId) {
        ExamTypePage createdExamType = this.examTypePageService.createExamTypeWithSchool(examTypePage, schoolId);
        return ResponseEntity.status((int)201).body(new ResponseMessage("Exam Type created successfully for school", createdExamType));
    }

    @PutMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> updateExamTypeWithSchool(@PathVariable String id, @PathVariable String schoolId, @RequestBody ExamTypePage updatedExamType) {
        ExamTypePage updated = this.examTypePageService.updateExamTypeWithSchool(id, updatedExamType, schoolId);
        if (updated != null) {
            return ResponseEntity.ok(new ResponseMessage("Exam Type updated successfully for school", updated));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Exam Type not found for this school", null));
    }

    @DeleteMapping(value={"/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage> deleteExamTypeWithSchool(@PathVariable String id, @PathVariable String schoolId) {
        boolean deleted = this.examTypePageService.deleteExamTypeWithSchool(id, schoolId);
        if (deleted) {
            return ResponseEntity.ok(new ResponseMessage("Exam Type deleted successfully for school", null));
        }
        return ResponseEntity.status((int)404).body(new ResponseMessage("Exam Type not found for this school", null));
    }
}

