/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.neel.Teacher.controller.AssignmentController
 *  com.neel.Teacher.model.Assignment
 *  com.neel.Teacher.model.AssignmentDto
 *  com.neel.Teacher.model.ResponseMessage
 *  com.neel.Teacher.service.AssignmentService
 *  org.springframework.beans.factory.annotation.Autowired
 *  org.springframework.format.annotation.DateTimeFormat
 *  org.springframework.format.annotation.DateTimeFormat$ISO
 *  org.springframework.http.HttpStatus
 *  org.springframework.http.HttpStatusCode
 *  org.springframework.http.ResponseEntity
 *  org.springframework.web.bind.annotation.CrossOrigin
 *  org.springframework.web.bind.annotation.DeleteMapping
 *  org.springframework.web.bind.annotation.GetMapping
 *  org.springframework.web.bind.annotation.ModelAttribute
 *  org.springframework.web.bind.annotation.PathVariable
 *  org.springframework.web.bind.annotation.PostMapping
 *  org.springframework.web.bind.annotation.PutMapping
 *  org.springframework.web.bind.annotation.RequestMapping
 *  org.springframework.web.bind.annotation.RestController
 */
package com.neel.Teacher.controller;

import com.neel.Teacher.model.Assignment;
import com.neel.Teacher.model.AssignmentDto;
import com.neel.Teacher.model.ResponseMessage;
import com.neel.Teacher.service.AssignmentService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value={"/api"})
@CrossOrigin(origins={"*"})
public class AssignmentController {
    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping(value={"/assignments"})
    public ResponseEntity<ResponseMessage<Assignment>> createAssignment(@ModelAttribute AssignmentDto assignmentDto) {
        try {
            Assignment assignment = this.assignmentService.createAssignment(assignmentDto);
            return ResponseEntity.ok((Object)new ResponseMessage("Assignment created successfully", (Object)assignment));
        }
        catch (IOException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body((Object)new ResponseMessage("Error creating assignment: " + e.getMessage(), null));
        }
    }

    @GetMapping(value={"/assignments"})
    public ResponseEntity<ResponseMessage<List<Assignment>>> getAllAssignments() {
        List assignments = this.assignmentService.getAllAssignments();
        return ResponseEntity.ok((Object)new ResponseMessage("Assignments retrieved successfully", (Object)assignments));
    }

    @GetMapping(value={"/assignments/school/{schoolId}"})
    public ResponseEntity<ResponseMessage<List<Assignment>>> getAssignmentsBySchool(@PathVariable String schoolId) {
        List assignments = this.assignmentService.getAssignmentsBySchool(schoolId);
        return ResponseEntity.ok((Object)new ResponseMessage("Assignments retrieved successfully", (Object)assignments));
    }

    @GetMapping(value={"/assignments/teacher/{teacherId}"})
    public ResponseEntity<ResponseMessage<List<Assignment>>> getAssignmentsByTeacher(@PathVariable String teacherId) {
        List assignments = this.assignmentService.getAssignmentsByTeacher(teacherId);
        return ResponseEntity.ok((Object)new ResponseMessage("Assignments retrieved successfully", (Object)assignments));
    }

    @GetMapping(value={"/assignments/school/{schoolId}/teacher/{teacherId}"})
    public ResponseEntity<ResponseMessage<List<Assignment>>> getAssignmentsBySchoolAndTeacher(@PathVariable String schoolId, @PathVariable String teacherId) {
        List assignments = this.assignmentService.getAssignmentsBySchoolAndTeacher(schoolId, teacherId);
        return ResponseEntity.ok((Object)new ResponseMessage("Assignments retrieved successfully", (Object)assignments));
    }

    @GetMapping(value={"/assignments/school/{schoolId}/class/{className}/section/{section}"})
    public ResponseEntity<ResponseMessage<List<Assignment>>> getAssignmentsByClass(@PathVariable String schoolId, @PathVariable String className, @PathVariable String section) {
        List assignments = this.assignmentService.getAssignmentsByClass(schoolId, className, section);
        return ResponseEntity.ok((Object)new ResponseMessage("Assignments retrieved successfully", (Object)assignments));
    }

    @GetMapping(value={"/assignments/school/{schoolId}/class/{className}/section/{section}/subject/{subject}"})
    public ResponseEntity<ResponseMessage<List<Assignment>>> getAssignmentsByClassAndSubject(@PathVariable String schoolId, @PathVariable String className, @PathVariable String section, @PathVariable String subject) {
        List assignments = this.assignmentService.getAssignmentsByClassAndSubject(schoolId, className, section, subject);
        return ResponseEntity.ok((Object)new ResponseMessage("Assignments retrieved successfully", (Object)assignments));
    }

    @GetMapping(value={"/assignments/school/{schoolId}/date/{date}"})
    public ResponseEntity<ResponseMessage<List<Assignment>>> getAssignmentsByDate(@PathVariable String schoolId, @PathVariable @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date) {
        List assignments = this.assignmentService.getAssignmentsByDate(schoolId, date);
        return ResponseEntity.ok((Object)new ResponseMessage("Assignments retrieved successfully", (Object)assignments));
    }

    @GetMapping(value={"/assignments/{id}"})
    public ResponseEntity<ResponseMessage<Assignment>> getAssignmentById(@PathVariable String id) {
        Optional assignment = this.assignmentService.getAssignmentById(id);
        if (assignment.isPresent()) {
            return ResponseEntity.ok((Object)new ResponseMessage("Assignment retrieved successfully", (Object)((Assignment)assignment.get())));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Assignment not found", null));
    }

    @PutMapping(value={"/assignments/{id}"})
    public ResponseEntity<ResponseMessage<Assignment>> updateAssignment(@PathVariable String id, @ModelAttribute AssignmentDto assignmentDto) {
        try {
            Assignment assignment = this.assignmentService.updateAssignment(id, assignmentDto);
            if (assignment != null) {
                return ResponseEntity.ok((Object)new ResponseMessage("Assignment updated successfully", (Object)assignment));
            }
            return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Assignment not found", null));
        }
        catch (IOException e) {
            return ResponseEntity.status((HttpStatusCode)HttpStatus.BAD_REQUEST).body((Object)new ResponseMessage("Error updating assignment: " + e.getMessage(), null));
        }
    }

    @DeleteMapping(value={"/assignments/{id}"})
    public ResponseEntity<ResponseMessage<Void>> deleteAssignment(@PathVariable String id) {
        boolean deleted = this.assignmentService.deleteAssignment(id);
        if (deleted) {
            return ResponseEntity.ok((Object)new ResponseMessage("Assignment deleted successfully", null));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Assignment not found", null));
    }

    @PutMapping(value={"/assignments/close/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Assignment>> closeAssignment(@PathVariable String id, @PathVariable String schoolId) {
        Assignment assignment = this.assignmentService.closeAssignment(id, schoolId);
        if (assignment != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("Assignment closed successfully", (Object)assignment));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Assignment not found", null));
    }

    @PutMapping(value={"/assignments/active/{id}/{schoolId}"})
    public ResponseEntity<ResponseMessage<Assignment>> activeAssignment(@PathVariable String id, @PathVariable String schoolId) {
        Assignment assignment = this.assignmentService.activeAssignment(id, schoolId);
        if (assignment != null) {
            return ResponseEntity.ok((Object)new ResponseMessage("Assignment activated successfully", (Object)assignment));
        }
        return ResponseEntity.status((HttpStatusCode)HttpStatus.NOT_FOUND).body((Object)new ResponseMessage("Assignment not found", null));
    }

    @GetMapping(value={"/assignments/school/{schoolId}/teacher/{teacherId}/due-today/count"})
    public ResponseEntity<ResponseMessage<Long>> countAssignmentsDueToday(@PathVariable String schoolId, @PathVariable String teacherId) {
        long count = this.assignmentService.countAssignmentsDueToday(schoolId, teacherId);
        return ResponseEntity.ok((Object)new ResponseMessage("Count of assignments due today retrieved successfully", (Object)count));
    }

    @GetMapping(value={"/teacher-summary/{teacherId}/{schoolId}"})
    public ResponseEntity<ResponseMessage> AllDashboardSummaryForTeacher(@PathVariable String schoolId, @PathVariable String teacherId) {
        HashMap<String, Long> counts = new HashMap<String, Long>();
        counts.put("activeAssignment", this.assignmentService.countActiveAssignmentBySchool(schoolId, teacherId));
        counts.put("closedAssignment", this.assignmentService.countClosedAssignmentBySchool(schoolId, teacherId));
        counts.put("dueDateAssignment", this.assignmentService.countAssignmentsDueToday(schoolId, teacherId));
        counts.put("allAssignment", this.assignmentService.allAssignmentCountBySchool(schoolId, teacherId));
        return new ResponseEntity((Object)new ResponseMessage("All dashboard summary count ", counts), (HttpStatusCode)HttpStatus.OK);
    }
}

